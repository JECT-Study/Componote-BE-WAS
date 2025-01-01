package ject.componote.domain.auth.error;

import org.springframework.http.HttpStatus;

public class OffensiveNicknameException extends AuthException {
    public OffensiveNicknameException(final String value) {
        super("닉네임에 금지된 단어가 포함되있습니다. 입력한 닉네임: " + value, HttpStatus.BAD_REQUEST);
    }
}
