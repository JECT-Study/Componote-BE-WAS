package ject.componote.infra.oauth.application.profile;

import ject.componote.infra.oauth.error.OAuthErrorResponseFactory;
import ject.componote.infra.oauth.error.OAuthServerException;
import ject.componote.infra.oauth.error.profile.OAuthProfileException;
import ject.componote.infra.oauth.error.profile.response.OAuthProfileErrorResponse;
import ject.componote.infra.oauth.model.OAuthProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

@Component
public class ProfileFailHandler {
    public Mono<OAuthProfileException> handle4xxError(final ClientResponse clientResponse,
                                                      final OAuthProvider oAuthProvider) {
        return clientResponse.bodyToMono(OAuthErrorResponseFactory.getProfileResponseClassFrom(oAuthProvider.name()))
                .map(OAuthProfileException::new);
    }

    public Mono<OAuthServerException> handle5xxError(final ClientResponse clientResponse,
                                                     final OAuthProvider oAuthProvider) {
        return clientResponse.bodyToMono(OAuthErrorResponseFactory.getProfileResponseClassFrom(oAuthProvider.name()))
                .map(OAuthProfileErrorResponse::getMessage)
                .map(OAuthServerException::new);
    }
}
