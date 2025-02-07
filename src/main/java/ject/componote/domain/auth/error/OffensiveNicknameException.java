package ject.componote.domain.auth.error;

import org.springframework.http.HttpStatus;

public class OffensiveNicknameException extends AuthException {
    public OffensiveNicknameException() {
        super("닉네임에 금지된 단어가 포함되있습니다.", HttpStatus.BAD_REQUEST);
    }
}
