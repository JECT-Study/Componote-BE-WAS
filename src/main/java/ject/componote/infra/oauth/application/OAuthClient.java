package ject.componote.infra.oauth.application;

import ject.componote.infra.oauth.application.authorize.OAuthAuthorizeService;
import ject.componote.infra.oauth.application.profile.OAuthProfileService;
import ject.componote.infra.oauth.application.token.OAuthTokenService;
import ject.componote.infra.oauth.dto.authorize.response.OAuthAuthorizePayload;
import ject.componote.infra.oauth.error.param.OAuthParamException;
import ject.componote.infra.oauth.model.OAuthProvider;
import ject.componote.infra.oauth.model.profile.OAuthProfile;
import ject.componote.infra.oauth.repository.InMemoryOAuthProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static ject.componote.infra.oauth.error.param.OAuthParamErrorCode.INVALID_AUTHORIZATION_CODE;
import static ject.componote.infra.oauth.error.param.OAuthParamErrorCode.INVALID_PROVIDER_NAME;

@RequiredArgsConstructor
@Service
public class OAuthClient {
    private final InMemoryOAuthProviderRepository inMemoryOAuthProviderRepository;
    private final OAuthAuthorizeService oAuthAuthorizeService;
    private final OAuthProfileService oAuthProfileService;
    private final OAuthTokenService oAuthTokenService;

    public OAuthAuthorizePayload getAuthorizePayload(final String providerName) {
        validateProviderType(providerName);
        final OAuthProvider oAuthProvider = getProviderByName(providerName);
        return oAuthAuthorizeService.getAuthorizePayload(oAuthProvider);
    }

    public Mono<OAuthProfile> getProfile(final String providerType, final String code) {
        validateAuthorizationCode(code);
        validateProviderType(providerType);
        final OAuthProvider oAuthProvider = getProviderByName(providerType);
        return oAuthTokenService.getToken(oAuthProvider, code)
                .flatMap(response -> oAuthProfileService.getProfile(oAuthProvider, response));
    }

    private OAuthProvider getProviderByName(final String providerName) {
        return inMemoryOAuthProviderRepository.findByProviderName(providerName);
    }

    private void validateAuthorizationCode(final String code) {
        if (isNullOrBlank(code)) {
            throw new OAuthParamException(INVALID_AUTHORIZATION_CODE);   // 예외 처리 커스텀 예정
        }
    }

    private void validateProviderType(final String providerName) {
        if (isNullOrBlank(providerName)) {
            throw new OAuthParamException(INVALID_PROVIDER_NAME);   // 예외 처리 커스텀 예정
        }
    }

    private boolean isNullOrBlank(final String value) {
        return value == null || value.isBlank();
    }
}
