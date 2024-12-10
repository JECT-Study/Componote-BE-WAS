package ject.componote.infra.oauth.error.token.response.detail;

import ject.componote.infra.oauth.error.token.response.OAuthTokenIssueErrorResponse;

public record GoogleTokenIssueErrorResponse(String error, String error_description) implements OAuthTokenIssueErrorResponse {
    @Override
    public String getErrorCode() {
        return error;
    }

    @Override
    public String getMessage() {
        return error_description;
    }
}
