package ject.componote.domain.comment.error;

import ject.componote.global.error.ComponoteException;
import org.springframework.http.HttpStatus;

public class CommentException extends ComponoteException {
    public CommentException(final String message, final HttpStatus status) {
        super(message, status);
    }
}
