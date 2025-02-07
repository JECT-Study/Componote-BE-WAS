package ject.componote.domain.auth.error;

import org.springframework.http.HttpStatus;

public class InvalidNicknameCharacterException extends AuthException {
    public InvalidNicknameCharacterException() {
        super("닉네임은 특수 문자, 한글 초성, 공백을 허용하지 않습니다.", HttpStatus.BAD_REQUEST);
    }
}
