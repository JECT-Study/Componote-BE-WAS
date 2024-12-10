package ject.componote.domain.auth.error;

import org.springframework.http.HttpStatus;

public class InvalidNicknameException extends AuthException {
    public InvalidNicknameException() {
        super("닉네임이 올바르지 않습니다.", HttpStatus.BAD_REQUEST);
    }
}
