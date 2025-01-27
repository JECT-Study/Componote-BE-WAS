package ject.componote.domain.member.error;

import ject.componote.global.error.ComponoteException;
import org.springframework.http.HttpStatus;

public class MemberException extends ComponoteException {
    public MemberException(final String message, final HttpStatus status) {
        super(message, status);
    }

}
