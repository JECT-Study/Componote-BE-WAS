package ject.componote.infra.oauth.error.param;

import ject.componote.infra.oauth.error.OAuthClientException;

public class OAuthParamException extends OAuthClientException {
    public OAuthParamException(final OAuthParamErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
