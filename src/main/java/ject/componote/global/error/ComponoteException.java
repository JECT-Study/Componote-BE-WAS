package ject.componote.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ComponoteException extends RuntimeException {
    private final HttpStatus status;

    public ComponoteException(final HttpStatus status) {
        this.status = status;
    }

    public ComponoteException(final String message, final HttpStatus status) {
        super(message);
        this.status = status;
    }

    public ComponoteException(final String message, final Throwable cause, final HttpStatus status) {
        super(message, cause);
        this.status = status;
    }

    public ComponoteException(final Throwable cause, final HttpStatus status) {
        super(cause);
        this.status = status;
    }
}
