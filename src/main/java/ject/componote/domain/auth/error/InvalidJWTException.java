package ject.componote.domain.auth.error;

import org.springframework.http.HttpStatus;

public class InvalidJWTException extends AuthException {
    public InvalidJWTException() {
        super("유효하지 않은 JWT입니다.", HttpStatus.BAD_REQUEST);
    }
}
