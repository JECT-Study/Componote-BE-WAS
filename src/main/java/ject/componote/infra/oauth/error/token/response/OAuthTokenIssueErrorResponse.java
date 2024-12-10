package ject.componote.infra.oauth.error.token.response;

public interface OAuthTokenIssueErrorResponse {
    String getErrorCode();
    String getMessage();
}
