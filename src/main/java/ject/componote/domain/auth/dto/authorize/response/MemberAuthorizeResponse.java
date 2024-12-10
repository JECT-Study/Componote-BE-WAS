package ject.componote.domain.auth.dto.authorize.response;

import ject.componote.infra.oauth.dto.authorize.response.OAuthAuthorizePayload;

public record MemberAuthorizeResponse(String method, String url) {
    public static MemberAuthorizeResponse from(final OAuthAuthorizePayload oAuthAuthorizePayload) {
        return new MemberAuthorizeResponse(oAuthAuthorizePayload.method().name(), oAuthAuthorizePayload.url().toString());
    }
}
