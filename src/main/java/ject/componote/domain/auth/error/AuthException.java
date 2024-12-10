package ject.componote.domain.auth.error;

import ject.componote.global.error.ComponoteException;
import org.springframework.http.HttpStatus;

public class AuthException extends ComponoteException {
    public AuthException(final String message, final HttpStatus status) {
        super(message, status);
    }
}
