package ject.componote.domain.auth.application;

import ject.componote.domain.auth.dao.MemberRepository;
import ject.componote.domain.auth.dao.MemberSummaryDao;
import ject.componote.domain.auth.domain.Member;
import ject.componote.domain.auth.dto.find.response.MemberSummaryResponse;
import ject.componote.domain.auth.dto.update.request.MemberEmailUpdateRequest;
import ject.componote.domain.auth.dto.update.request.MemberNicknameUpdateRequest;
import ject.componote.domain.auth.dto.update.request.MemberProfileImageUpdateRequest;
import ject.componote.domain.auth.dto.verify.request.MemberEmailVerificationRequest;
import ject.componote.domain.auth.error.DuplicatedNicknameException;
import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.auth.model.Email;
import ject.componote.domain.auth.model.Nickname;
import ject.componote.domain.auth.model.ProfileImage;
import ject.componote.infra.file.application.FileService;
import ject.componote.infra.mail.application.MailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static ject.componote.fixture.MemberFixture.KIM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @Mock
    MailService mailService;

    @Mock
    FileService fileService;

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    MemberService memberService;

    Member member;
    AuthPrincipal authPrincipal;

    @BeforeEach
    public void init() {
        member = KIM.생성(1L);
        authPrincipal = AuthPrincipal.from(member);
    }

    @Test
    @DisplayName("회원 간단 조회")
    public void getMemberSummary() throws Exception {
        // given
        final Long memberId = member.getId();
        final MemberSummaryDao memberSummaryDao = new MemberSummaryDao(member.getEmail(), member.getNickname(), member.getProfileImage());
        final MemberSummaryResponse expect = MemberSummaryResponse.from(memberSummaryDao);

        // when
        doReturn(Optional.of(memberSummaryDao)).when(memberRepository)
                .findSummaryById(memberId);
        final MemberSummaryResponse actual = memberService.getMemberSummary(authPrincipal);

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    @DisplayName("프로필 사진 변경")
    public void updateProfileImage() throws Exception {
        // given
        final Long memberId = member.getId();
        final String newObjectKey = "new.jpg";
        final MemberProfileImageUpdateRequest request = new MemberProfileImageUpdateRequest(newObjectKey);
        final ProfileImage newProfileImage = ProfileImage.from(newObjectKey);

        // when
        doReturn(Optional.of(member)).when(memberRepository)
                .findById(memberId);
        doNothing().when(fileService)
                .moveImage(newProfileImage);
        memberService.updateProfileImage(authPrincipal, request);

        // then
        assertThat(member.equalsProfileImage(newProfileImage)).isTrue();
    }

    @Test
    @DisplayName("닉네임 변경")
    public void updateNickname() throws Exception {
        // given
        final Long memberId = member.getId();
        final String newNicknameValue = "newNick";
        final Nickname newNickname = Nickname.from(newNicknameValue);
        final MemberNicknameUpdateRequest request = new MemberNicknameUpdateRequest(newNicknameValue);

        // when
        doReturn(Optional.of(member)).when(memberRepository)
                .findById(memberId);
        doReturn(false).when(memberRepository)
                .existsByNickname(newNickname);
        memberService.updateNickname(authPrincipal, request);

        // then
        assertThat(member.equalsNickname(newNickname)).isTrue();
    }

    @Test
    @DisplayName("이메일 등록/변경")
    public void updateEmail() throws Exception {
        // given
        final Long memberId = member.getId();
        final String newEmailValue = "new@naver.com";
        final String verificationCode = "123abc";
        final Email newEmail = Email.from(newEmailValue);
        final MemberEmailUpdateRequest request = new MemberEmailUpdateRequest(newEmailValue, verificationCode);

        // when
        doReturn(Optional.of(member)).when(memberRepository)
                .findById(memberId);
        doReturn(false).when(memberRepository)
                .existsByEmail(newEmail);
        doNothing().when(mailService)
                .verifyEmailCode(newEmailValue, verificationCode);
        memberService.updateEmail(authPrincipal, request);

        // then
        assertThat(member.equalsEmail(newEmail)).isTrue();
    }

    @Test
    @DisplayName("이메일 인증 코드 전송")
    public void sendVerificationCode() throws Exception {
        // given
        final Long memberId = member.getId();
        final String newEmailValue = "new@naver.com";
        final Email newEmail = Email.from(newEmailValue);
        final MemberEmailVerificationRequest request = new MemberEmailVerificationRequest(newEmailValue);

        // when
        doReturn(Optional.of(member)).when(memberRepository)
                .findById(memberId);
        doReturn(false).when(memberRepository)
                .existsByEmail(newEmail);
        doNothing().when(mailService)
                .sendVerificationCode(newEmailValue);

        // then
        assertDoesNotThrow(
                () -> memberService.sendVerificationCode(authPrincipal, request)
        );
    }

    @Test
    @DisplayName("닉네임 변경 시 이미 닉네임이 존재하는 경우 예외 발생")
    public void updateNicknameWhenAlreadyExist() throws Exception {
        // given
        final Long memberId = member.getId();
        final String newNicknameValue = "newNick";
        final Nickname newNickname = Nickname.from(newNicknameValue);
        final MemberNicknameUpdateRequest request = new MemberNicknameUpdateRequest(newNicknameValue);

        // when
        doReturn(Optional.of(member)).when(memberRepository)
                .findById(memberId);
        doReturn(true).when(memberRepository)
                .existsByNickname(newNickname);

        // then
        assertThatThrownBy(() -> memberService.updateNickname(authPrincipal, request))
                .isInstanceOf(DuplicatedNicknameException.class);
    }
}