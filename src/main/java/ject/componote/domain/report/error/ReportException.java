package ject.componote.domain.report.error;

import ject.componote.global.error.ComponoteException;
import org.springframework.http.HttpStatus;

public class ReportException extends ComponoteException {
    public ReportException(final String message, final HttpStatus status) {
        super(message, status);
    }
}
