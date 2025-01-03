package ject.componote.infra.oauth.dto.token.request;

import ject.componote.infra.oauth.model.OAuthProvider;

public record OAuthTokenRequest(String grant_type,
                                String client_id,
                                String redirect_uri,
                                String code,
                                String client_secret) {
    public static OAuthTokenRequest of(final OAuthProvider provider, final String code) {
        return new OAuthTokenRequest(provider.grantType(), provider.clientId(), provider.redirectUri(), code, provider.clientSecret());
    }
}
