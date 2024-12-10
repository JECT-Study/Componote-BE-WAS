package ject.componote.infra.oauth.error;

import ject.componote.infra.error.InfraException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class OAuthClientException extends InfraException {
    public OAuthClientException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
