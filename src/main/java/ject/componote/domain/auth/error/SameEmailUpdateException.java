package ject.componote.domain.auth.error;

import org.springframework.http.HttpStatus;

public class SameEmailUpdateException extends AuthException {
    public SameEmailUpdateException() {
        super("이메일 주소가 기존에 등록된 이메일과 동일합니다.", HttpStatus.BAD_REQUEST);
    }
}
