package ject.componote.infra.oauth.error;

import ject.componote.infra.error.InfraException;
import org.springframework.http.HttpStatus;

public class OAuthServerException extends InfraException {
    public OAuthServerException(final String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
