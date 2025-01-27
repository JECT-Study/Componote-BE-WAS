package ject.componote.domain.auth.application;

import ject.componote.domain.member.dao.MemberRepository;
import ject.componote.domain.member.dao.SocialAccountRepository;
import ject.componote.domain.auth.domain.Member;
import ject.componote.domain.auth.dto.login.request.MemberLoginRequest;
import ject.componote.domain.auth.dto.login.response.MemberLoginResponse;
import ject.componote.domain.auth.dto.signup.request.MemberSignupRequest;
import ject.componote.domain.auth.dto.signup.response.MemberSignupResponse;
import ject.componote.domain.auth.dto.validate.request.MemberNicknameValidateRequest;
import ject.componote.domain.member.error.DuplicatedNicknameException;
import ject.componote.domain.member.error.DuplicatedSignupException;
import ject.componote.domain.member.error.NotFoundMemberException;
import ject.componote.domain.member.error.NotFoundSocialAccountException;
import ject.componote.domain.member.model.AuthPrincipal;
import ject.componote.domain.member.model.Nickname;
import ject.componote.domain.auth.token.application.TokenService;
import ject.componote.infra.storage.application.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuthService {
    private final StorageService storageService;
    private final MemberRepository memberRepository;
    private final SocialAccountRepository socialAccountRepository;
    private final TokenService tokenService;

    @Transactional
    public MemberSignupResponse signup(final MemberSignupRequest request) {
        final Long socialAccountId = getSocialAccountId(request.socialAccountToken());
        if (!socialAccountRepository.existsById(socialAccountId)) {
            throw new NotFoundSocialAccountException();
        }

        if (memberRepository.existsBySocialAccountId(socialAccountId)) {
            throw new DuplicatedSignupException();
        }

        final Member member = memberRepository.save(request.toMember(socialAccountId));
        storageService.moveImage(member.getProfileImage());
        final String accessToken = createAccessToken(member);
        return MemberSignupResponse.of(accessToken, member);
    }

    public MemberLoginResponse login(final MemberLoginRequest request) {
        final Long socialAccountId = getSocialAccountId(request.socialAccountToken());
        final Member member = findMemberBySocialAccountId(socialAccountId);
        final String accessToken = createAccessToken(member);
        return MemberLoginResponse.of(accessToken, member);
    }

    public void validateNickname(final MemberNicknameValidateRequest request) {
        final Nickname nickname = Nickname.from(request.nickname());
        if (memberRepository.existsByNickname(nickname)) {
            throw new DuplicatedNicknameException(nickname);
        }
    }

    private Member findMemberBySocialAccountId(final Long socialAccountId) {
        return memberRepository.findBySocialAccountId(socialAccountId)
                .orElseThrow(NotFoundMemberException::createWhenInvalidSocialAccountId);
    }

    private String createAccessToken(final Member member) {
        return tokenService.createAccessToken(AuthPrincipal.from(member));
    }

    private Long getSocialAccountId(final String socialAccountToken) {
        return tokenService.extractSocialAccountTokenPayload(socialAccountToken);
    }
}
