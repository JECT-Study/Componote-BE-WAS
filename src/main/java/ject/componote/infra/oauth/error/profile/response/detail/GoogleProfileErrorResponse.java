package ject.componote.infra.oauth.error.profile.response.detail;

import ject.componote.infra.oauth.error.profile.response.OAuthProfileErrorResponse;

public record GoogleProfileErrorResponse(Error error) implements OAuthProfileErrorResponse {
    @Override
    public String getErrorCode() {
        return error.code;
    }

    @Override
    public String getMessage() {
        return error.message;
    }

    record Error(String code, String message, String status) {
    }
}
