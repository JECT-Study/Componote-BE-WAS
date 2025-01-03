package ject.componote.infra.file.error;

import ject.componote.infra.error.InfraException;
import org.springframework.http.HttpStatus;

public class FileClientException extends InfraException {
    public FileClientException(final FileServerErrorResponse response) {
        super(response.getMessage(), HttpStatus.valueOf(response.getStatus()));
    }
}
