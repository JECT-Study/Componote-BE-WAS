package ject.componote.domain.auth.error;

import org.springframework.http.HttpStatus;

public class ExpiredJWTException extends AuthException {
    public ExpiredJWTException() {
        super("만료된 JWT입니다.", HttpStatus.BAD_REQUEST);
    }
}
