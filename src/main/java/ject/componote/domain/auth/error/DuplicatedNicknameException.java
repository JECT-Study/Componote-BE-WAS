package ject.componote.domain.auth.error;

import ject.componote.domain.auth.model.Nickname;
import org.springframework.http.HttpStatus;

public class DuplicatedNicknameException extends AuthException {
    public DuplicatedNicknameException(final Nickname nickname) {
        super("이미 존재하는 닉네임입니다. 입력한 닉네임: " + nickname.getValue(), HttpStatus.BAD_REQUEST);
    }
}
