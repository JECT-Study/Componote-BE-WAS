package ject.componote.infra.mail.error;

import ject.componote.infra.error.InfraException;
import org.springframework.http.HttpStatus;

public class MailException extends InfraException {
    public MailException(final String message, final HttpStatus status) {
        super(message, status);
    }
}
