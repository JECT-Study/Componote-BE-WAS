package ject.componote.domain.auth.error;

import ject.componote.domain.auth.model.Nickname;
import org.springframework.http.HttpStatus;

public class InvalidNicknameLengthException extends AuthException {
    public InvalidNicknameLengthException() {
        super(
                String.format("닉네임 길이는 %d ~ %d 글자 사이여야 합니다.", Nickname.MIN_LENGTH, Nickname.MAX_LENGTH),
                HttpStatus.BAD_REQUEST
        );
    }
}
