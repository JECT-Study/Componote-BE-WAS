package ject.componote.domain.auth.error;

import org.springframework.http.HttpStatus;

public class InvalidEncryptionSecretKeyException extends AuthException {
    public InvalidEncryptionSecretKeyException(final String secretKey) {
        super("유효하지 않은 시크릿 키입니다. 현재 시크릿 키: " + secretKey, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
