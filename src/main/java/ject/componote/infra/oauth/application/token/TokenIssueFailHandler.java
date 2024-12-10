package ject.componote.infra.oauth.application.token;

import ject.componote.infra.oauth.error.OAuthErrorResponseFactory;
import ject.componote.infra.oauth.error.OAuthServerException;
import ject.componote.infra.oauth.error.token.OAuthTokenIssueException;
import ject.componote.infra.oauth.error.token.response.OAuthTokenIssueErrorResponse;
import ject.componote.infra.oauth.model.OAuthProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

@Component
public class TokenIssueFailHandler {
    public Mono<OAuthTokenIssueException> handle4xxError(final ClientResponse clientResponse,
                                                         final OAuthProvider oAuthProvider) {
        return clientResponse.bodyToMono(OAuthErrorResponseFactory.getTokenIssueResponseClassFrom(oAuthProvider.name()))
                .map(OAuthTokenIssueException::new);
    }

    public Mono<OAuthServerException> handle5xxError(final ClientResponse clientResponse,
                                                     final OAuthProvider oAuthProvider) {
        return clientResponse.bodyToMono(OAuthErrorResponseFactory.getTokenIssueResponseClassFrom(oAuthProvider.name()))
                .map(OAuthTokenIssueErrorResponse::getMessage)
                .map(OAuthServerException::new);
    }
}
