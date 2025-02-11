package ject.componote.domain.auth.application;

import ject.componote.domain.auth.dao.MemberRepository;
import ject.componote.domain.auth.domain.Job;
import ject.componote.domain.auth.domain.Member;
import ject.componote.domain.auth.dto.find.response.MemberSummaryResponse;
import ject.componote.domain.auth.dto.image.event.ProfileImageMoveEvent;
import ject.componote.domain.auth.dto.update.request.MemberEmailUpdateRequest;
import ject.componote.domain.auth.dto.update.request.MemberUpdateRequest;
import ject.componote.domain.auth.dto.verify.event.EmailVerificationCodeSendEvent;
import ject.componote.domain.auth.dto.verify.request.MemberEmailVerificationRequest;
import ject.componote.domain.auth.error.DuplicatedEmailException;
import ject.componote.domain.auth.error.DuplicatedNicknameException;
import ject.componote.domain.auth.error.NotFoundMemberException;
import ject.componote.domain.auth.error.SameEmailUpdateException;
import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.auth.model.Email;
import ject.componote.domain.auth.model.Nickname;
import ject.componote.domain.auth.model.ProfileImage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final ApplicationEventPublisher eventPublisher;
    private final MemberRepository memberRepository;
    private final VerificationCodeService verificationCodeService;

    public MemberSummaryResponse getMemberSummary(final AuthPrincipal authPrincipal) {
        final Long memberId = authPrincipal.id();
        return memberRepository.findSummaryById(memberId)
                .map(MemberSummaryResponse::from)
                .orElseThrow(() -> NotFoundMemberException.createWhenInvalidMemberId(memberId));
    }

    @Transactional
    public void updateMember(final AuthPrincipal authPrincipal, final MemberUpdateRequest request) {
        final Member member = findMemberById(authPrincipal.id());
        updateProfileImage(member, request.profileImageObjectKey());
        updateJob(member, request.job());
        updateNickname(member, request.nickname());
    }

    @Transactional
    public void updateEmail(final AuthPrincipal authPrincipal, final MemberEmailUpdateRequest request) {
        final Member member = findMemberById(authPrincipal.id());
        final Email email = Email.from(request.email());
        validateSameEmail(member, email);
        validateDuplicatedEmail(email);
        verificationCodeService.verifyEmailCode(request.email(), request.verificationCode());
        member.updateEmail(email);
    }

    private void updateProfileImage(final Member member, final String profileImageObjectKey) {
        final ProfileImage profileImage = ProfileImage.from(profileImageObjectKey);
        if (member.equalsProfileImage(profileImage)) {
            return;
        }

        member.updateProfileImage(profileImage);
        eventPublisher.publishEvent(ProfileImageMoveEvent.from(member));
    }

    private void updateJob(final Member member, final Job job) {
        member.updateJob(job);
    }

    private void updateNickname(final Member member, final String nicknameValue) {
        final Nickname nickname = Nickname.from(nicknameValue);
        if (member.equalsNickname(nickname)) {
            return;
        }

        if (memberRepository.existsByNickname(nickname)) {
            throw new DuplicatedNicknameException(nickname);
        }

        member.updateNickname(nickname);
    }

    public void sendVerificationCode(final AuthPrincipal authPrincipal, final MemberEmailVerificationRequest request) {
        final Email email = Email.from(request.email());
        validateDuplicatedEmail(email);

        final Member member = findMemberById(authPrincipal.id());
        validateSameEmail(member, email);

        eventPublisher.publishEvent(EmailVerificationCodeSendEvent.from(email));
    }

    private Member findMemberById(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> NotFoundMemberException.createWhenInvalidMemberId(memberId));
    }

    private void validateSameEmail(final Member member, final Email email) {
        if (member.equalsEmail(email)) {
            throw new SameEmailUpdateException();
        }
    }

    private void validateDuplicatedEmail(final Email email) {
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicatedEmailException(email);
        }
    }
}
