package ject.componote.domain.auth.application;

import ject.componote.domain.auth.dao.MemberRepository;
import ject.componote.domain.auth.dao.SocialAccountRepository;
import ject.componote.domain.auth.domain.Member;
import ject.componote.domain.auth.dto.login.request.MemberLoginRequest;
import ject.componote.domain.auth.dto.login.response.MemberLoginResponse;
import ject.componote.domain.auth.dto.signup.request.MemberSignupRequest;
import ject.componote.domain.auth.dto.signup.response.MemberSignupResponse;
import ject.componote.domain.auth.error.DuplicatedSignupException;
import ject.componote.domain.auth.error.NotFoundMemberException;
import ject.componote.domain.auth.error.NotFoundSocialAccountException;
import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.auth.model.ProfileImage;
import ject.componote.domain.auth.util.TokenProvider;
import ject.componote.infra.storage.application.StorageService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    StorageService storageService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    SocialAccountRepository socialAccountRepository;

    @Mock
    TokenProvider tokenProvider;

    @InjectMocks
    AuthService authService;

    Long socialAccountId = 1L;
    Member member = KIM.생성(socialAccountId);
    ProfileImage profileImage = member.getProfileImage();
    String profileImageObjectKey = profileImage.getObjectKey();

    @DisplayName("회원 가입")
    @Test
    public void signup() throws Exception {
        // given
        final MemberSignupRequest request = new MemberSignupRequest(
                member.getNickname().getValue(),
                member.getJob().name(),
                profileImageObjectKey,
                socialAccountId
        );
        final MemberSignupResponse expect = MemberSignupResponse.from(member);

        // when
        doReturn(true).when(socialAccountRepository)
                .existsById(socialAccountId);
        doReturn(false).when(memberRepository)
                .existsBySocialAccountId(socialAccountId);
        doReturn(member).when(memberRepository)
                .save(any());
        doNothing().when(storageService)
                .moveImage(profileImage);
        final MemberSignupResponse actual = authService.signup(request);

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @DisplayName("회원 가입 시 이미 가입된 회원이면 예외 발생")
    @Test
    public void signupWhenAlreadyExist() throws Exception {
        // given
        final MemberSignupRequest request = new MemberSignupRequest(
                member.getNickname().getValue(),
                member.getJob().name(),
                profileImageObjectKey,
                socialAccountId
        );

        // when
        doReturn(true).when(socialAccountRepository)
                .existsById(socialAccountId);
        doReturn(true).when(memberRepository)
                .existsBySocialAccountId(socialAccountId);

        // then
        assertThatThrownBy(() -> authService.signup(request))
                .isInstanceOf(DuplicatedSignupException.class);
    }

    @DisplayName("회원 가입 시 소셜 ID가 잘못된 경우 예외 발생")
    @Test
    public void signupWhenInvalidSocialAccountId() throws Exception {
        // given
        final MemberSignupRequest request = new MemberSignupRequest(
                member.getNickname().getValue(),
                member.getJob().name(),
                profileImageObjectKey,
                socialAccountId
        );

        // when
        doReturn(false).when(socialAccountRepository)
                .existsById(socialAccountId);

        // then
        assertThatThrownBy(() -> authService.signup(request))
                .isInstanceOf(NotFoundSocialAccountException.class);
    }

    @DisplayName("로그인")
    @Test
    public void login() throws Exception {
        // given
        final String accessToken = "accessToken";
        final MemberLoginRequest request = new MemberLoginRequest(socialAccountId);
        final MemberLoginResponse expect = MemberLoginResponse.of(accessToken, member);

        // when
        doReturn(Optional.of(member)).when(memberRepository)
                .findBySocialAccountId(socialAccountId);
        doReturn(accessToken).when(tokenProvider)
                .createToken(AuthPrincipal.from(member));
        final MemberLoginResponse actual = authService.login(request);

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @DisplayName("로그인 시 소셜 ID에 해당하는 회원이 없으면 예외 발생")
    @Test
    public void loginWhenInvalidSocialAccountId() throws Exception {
        // given
        final MemberLoginRequest request = new MemberLoginRequest(socialAccountId);

        // when
        doReturn(Optional.empty()).when(memberRepository)
                .findBySocialAccountId(socialAccountId);

        // then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(NotFoundMemberException.class);
    }
}