package ject.componote.domain.auth.error;

import org.springframework.http.HttpStatus;

public class NotFoundJWTException extends AuthException {
    public NotFoundJWTException() {
        super("JWT를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
