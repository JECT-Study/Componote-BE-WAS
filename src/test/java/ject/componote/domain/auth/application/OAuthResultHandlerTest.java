package ject.componote.domain.auth.application;

import ject.componote.domain.member.dao.SocialAccountRepository;
import ject.componote.domain.member.domain.SocialAccount;
import ject.componote.domain.auth.util.SocialAccountMapper;
import ject.componote.infra.oauth.model.OAuthProvider;
import ject.componote.infra.oauth.model.profile.OAuthProfile;
import ject.componote.infra.oauth.model.profile.OAuthProfileFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ject.componote.fixture.SocialAccountFixture.KIM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class OAuthResultHandlerTest {
    @Mock
    SocialAccountRepository socialAccountRepository;

    @Mock
    SocialAccountMapper socialAccountMapper;

    @InjectMocks
    OAuthResultHandler oAuthResultHandler;

    SocialAccount socialAccount = KIM.생성();

    @ParameterizedTest
    @DisplayName("소셜 프로필 정보가 DB에 없으면 저장")
    @ValueSource(strings = {"google", "github", "naver"})
    public void save(final String providerType) throws Exception {
        // given
        final OAuthProvider oAuthProvider = getOAuthProvider(providerType);
        final OAuthProfile oAuthProfile = getOAuthProfile(oAuthProvider);
        final SocialAccount expect = socialAccount;

        // when
        doReturn(Optional.empty()).when(socialAccountRepository)
                .findBySocialIdAndProviderType(oAuthProfile.getSocialId(), oAuthProfile.getProviderType());
        doReturn(expect).when(socialAccountMapper)
                .mapFrom(oAuthProfile);
        doReturn(expect).when(socialAccountRepository)
                .save(expect);
        final SocialAccount actual = oAuthResultHandler.saveOrGet(oAuthProfile);

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @ParameterizedTest
    @DisplayName("소셜 프로필 정보가 DB에 있으면 조회")
    @ValueSource(strings = {"google", "github", "naver"})
    public void get(final String providerType) throws Exception {
        // given
        final OAuthProvider oAuthProvider = getOAuthProvider(providerType);
        final OAuthProfile oAuthProfile = getOAuthProfile(oAuthProvider);
        final SocialAccount expect = socialAccount;

        // when
        doReturn(Optional.of(expect)).when(socialAccountRepository)
                .findBySocialIdAndProviderType(oAuthProfile.getSocialId(), oAuthProfile.getProviderType());
        final SocialAccount actual = oAuthResultHandler.saveOrGet(oAuthProfile);

        // then
        assertThat(actual).isEqualTo(expect);
    }


    private OAuthProvider getOAuthProvider(final String providerType) {
        return new OAuthProvider(
                providerType,
                "clientId",
                "clientSecret",
                "code",
                "authorizeUrl",
                HttpMethod.GET,
                List.of("profile", "image"),
                "redirectUrl",
                "grantType",
                HttpMethod.GET,
                "tokenUrl",
                HttpMethod.GET,
                "profileUrl"
        );
    }

    private OAuthProfile getOAuthProfile(final OAuthProvider oAuthProvider) {
        return OAuthProfileFactory.of(
                Map.of(
                        "response", Map.of("id", "social_id"),
                        "id", "social_id"
                )
                , oAuthProvider
        );
    }
}