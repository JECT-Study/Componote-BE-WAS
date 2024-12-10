package ject.componote.infra.oauth.error.profile.response.detail;

import ject.componote.infra.oauth.error.profile.response.OAuthProfileErrorResponse;

public record KakaoProfileErrorResponse(Integer code, String msg) implements OAuthProfileErrorResponse {
    @Override
    public String getErrorCode() {
        return code.toString();
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
