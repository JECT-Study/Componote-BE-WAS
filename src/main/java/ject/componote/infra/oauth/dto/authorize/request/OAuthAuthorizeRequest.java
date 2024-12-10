package ject.componote.infra.oauth.dto.authorize.request;

import ject.componote.infra.oauth.model.OAuthProvider;

import java.util.List;

public record OAuthAuthorizeRequest(String client_id, String response_type, List<String> scope, String redirect_uri) {
    public static OAuthAuthorizeRequest from(final OAuthProvider oAuthProvider) {
        return new OAuthAuthorizeRequest(oAuthProvider.clientId(), oAuthProvider.responseType(), oAuthProvider.scope(), oAuthProvider.redirectUri());
    }
}
