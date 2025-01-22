package ject.componote.domain.auth.application;

import ject.componote.domain.auth.dao.MemberRepository;
import ject.componote.domain.auth.dao.SocialAccountRepository;
import ject.componote.domain.auth.domain.Member;
import ject.componote.domain.auth.dto.login.request.MemberLoginRequest;
import ject.componote.domain.auth.dto.login.response.MemberLoginResponse;
import ject.componote.domain.auth.dto.signup.request.MemberSignupRequest;
import ject.componote.domain.auth.dto.signup.response.MemberSignupResponse;
import ject.componote.domain.auth.dto.validate.request.MemberNicknameValidateRequest;
import ject.componote.domain.auth.error.DuplicatedNicknameException;
import ject.componote.domain.auth.error.DuplicatedSignupException;
import ject.componote.domain.auth.error.NotFoundMemberException;
import ject.componote.domain.auth.error.NotFoundSocialAccountException;
import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.auth.model.Nickname;
import ject.componote.domain.auth.util.AESCryptography;
import ject.componote.domain.auth.util.TokenProvider;
import ject.componote.infra.storage.application.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuthService {
    private final AESCryptography aesCryptography;
    private final StorageService storageService;
    private final MemberRepository memberRepository;
    private final SocialAccountRepository socialAccountRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public MemberSignupResponse signup(final MemberSignupRequest request) {
        final Long socialAccountId = parseSocialAccountId(request.encryptedSocialAccountId());
        if (!socialAccountRepository.existsById(socialAccountId)) {
            throw new NotFoundSocialAccountException(socialAccountId);
        }

        if (memberRepository.existsBySocialAccountId(socialAccountId)) {
            throw new DuplicatedSignupException(socialAccountId);
        }

        final Member member = memberRepository.save(request.toMember(socialAccountId));
        final String accessToken = tokenProvider.createToken(AuthPrincipal.from(member));
        storageService.moveImage(member.getProfileImage());
        return MemberSignupResponse.of(accessToken, member);
    }

    public MemberLoginResponse login(final MemberLoginRequest request) {
        final Long socialAccountId = parseSocialAccountId(request.encryptedSocialAccountId());
        final Member member = findMemberBySocialAccountId(socialAccountId);
        final String accessToken = tokenProvider.createToken(AuthPrincipal.from(member));
        return MemberLoginResponse.of(accessToken, member);
    }

    private Long parseSocialAccountId(final String encryptedSocialAccountId) {
        return aesCryptography.decrypt(encryptedSocialAccountId, Long::valueOf);
    }

    public void validateNickname(final MemberNicknameValidateRequest request) {
        final Nickname nickname = Nickname.from(request.nickname());
        if (memberRepository.existsByNickname(nickname)) {
            throw new DuplicatedNicknameException(nickname);
        }
    }

    private Member findMemberBySocialAccountId(final Long socialAccountId) {
        return memberRepository.findBySocialAccountId(socialAccountId)
                .orElseThrow(() -> NotFoundMemberException.createWhenInvalidSocialAccountId(socialAccountId));
    }
}
