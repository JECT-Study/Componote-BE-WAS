package ject.componote.domain.auth.error;

import org.springframework.http.HttpStatus;

public class ForbiddenJWTException extends AuthException {
    public ForbiddenJWTException() {
        super("JWT에 인가 권한이 없습니다.", HttpStatus.FORBIDDEN);
    }
}
