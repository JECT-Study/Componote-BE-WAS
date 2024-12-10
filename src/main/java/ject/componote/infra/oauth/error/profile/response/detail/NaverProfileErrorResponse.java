package ject.componote.infra.oauth.error.profile.response.detail;

import ject.componote.infra.oauth.error.profile.response.OAuthProfileErrorResponse;

public record NaverProfileErrorResponse(String resultCode, String message) implements OAuthProfileErrorResponse {
    @Override
    public String getErrorCode() {
        return resultCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
