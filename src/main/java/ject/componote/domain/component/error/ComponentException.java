package ject.componote.domain.component.error;

import ject.componote.global.error.ComponoteException;
import org.springframework.http.HttpStatus;

public class ComponentException extends ComponoteException {
    public ComponentException(final String message, final HttpStatus status) {
        super(message, status);
    }
}
