package ject.componote.domain.auth.error;

import ject.componote.domain.member.model.Email;
import org.springframework.http.HttpStatus;

public class DuplicatedEmailException extends AuthException {
    public DuplicatedEmailException(final Email email) {
        super("이미 존재하는 이메일입니다. 입력한 이메일: " + email.getValue(), HttpStatus.BAD_REQUEST);
    }
}
