package ject.componote.domain.design.error;

import ject.componote.global.error.ComponoteException;
import org.springframework.http.HttpStatus;

public class DesignException extends ComponoteException {
    public DesignException(final String message, final HttpStatus status) {
        super(message, status);
    }
}
