package ject.componote.domain.auth.application;

import ject.componote.domain.auth.dao.MemberRepository;
import ject.componote.domain.auth.domain.Member;
import ject.componote.domain.auth.dto.find.response.MemberSummaryResponse;
import ject.componote.domain.auth.dto.update.request.MemberEmailUpdateRequest;
import ject.componote.domain.auth.dto.update.request.MemberNicknameUpdateRequest;
import ject.componote.domain.auth.dto.update.request.MemberProfileImageUpdateRequest;
import ject.componote.domain.auth.dto.verify.request.MemberEmailVerificationRequest;
import ject.componote.domain.auth.error.DuplicatedEmailException;
import ject.componote.domain.auth.error.DuplicatedNicknameException;
import ject.componote.domain.auth.error.NotFoundMemberException;
import ject.componote.domain.auth.error.SameEmailUpdateException;
import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.auth.model.Email;
import ject.componote.domain.auth.model.Nickname;
import ject.componote.domain.auth.model.ProfileImage;
import ject.componote.infra.file.application.FileService;
import ject.componote.infra.mail.application.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final FileService fileService;
    private final MailService mailService;
    private final MemberRepository memberRepository;

    public MemberSummaryResponse getMemberSummary(final AuthPrincipal authPrincipal) {
        final Long memberId = authPrincipal.id();
        return memberRepository.findSummaryById(memberId)
                .map(MemberSummaryResponse::from)
                .orElseThrow(() -> NotFoundMemberException.createWhenInvalidMemberId(memberId));
    }

    @Transactional
    public void updateProfileImage(final AuthPrincipal authPrincipal, final MemberProfileImageUpdateRequest request) {
        final Member member = findMemberById(authPrincipal.id());
        final ProfileImage profileImage = ProfileImage.from(request.profileImageObjectKey());
        if (member.equalsProfileImage(profileImage)) {
            return;
        }

        fileService.moveImage(profileImage);
        member.updateProfileImage(profileImage);
        memberRepository.save(member);
    }

    @Transactional
    public void updateNickname(final AuthPrincipal authPrincipal, final MemberNicknameUpdateRequest request) {
        final Member member = findMemberById(authPrincipal.id());
        final Nickname nickname = Nickname.from(request.nickname());
        if (member.equalsNickname(nickname)) {
            return;
        }

        if (memberRepository.existsByNickname(nickname)) {
            throw new DuplicatedNicknameException(nickname);
        }

        member.updateNickname(nickname);
        memberRepository.save(member);
    }

    @Transactional
    public void updateEmail(final AuthPrincipal authPrincipal, final MemberEmailUpdateRequest request) {
        validateDuplicatedEmail(request.email());
        mailService.verifyEmailCode(request.email(), request.verificationCode());

        final Email email = Email.from(request.email());
        final Member member = findMemberById(authPrincipal.id());
        validateSameEmail(member, email);
        member.updateEmail(email);
    }

    public void sendVerificationCode(final MemberEmailVerificationRequest request) {
        validateDuplicatedEmail(request.email());
        mailService.sendVerificationCode(request.email());
    }

    private Member findMemberById(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> NotFoundMemberException.createWhenInvalidMemberId(memberId));
    }

    private void validateSameEmail(final Member member, final Email newEmail) {
        if (member.equalsEmail(newEmail)) {
            throw new SameEmailUpdateException();
        }
    }

    private void validateDuplicatedEmail(final String emailValue) {
        final Email email = Email.from(emailValue);
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicatedEmailException(email);
        }
    }
}
