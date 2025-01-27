package ject.componote.domain.member.error;

import org.springframework.http.HttpStatus;

public class InvalidEmailException extends MemberException {
    public InvalidEmailException(final String value) {
        super("이메일 형식이 올바르지 않습니다. 입력한 이메일: " + value, HttpStatus.BAD_REQUEST);
    }
}
