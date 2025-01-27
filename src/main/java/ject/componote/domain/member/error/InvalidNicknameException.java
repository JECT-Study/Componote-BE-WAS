package ject.componote.domain.member.error;

import org.springframework.http.HttpStatus;

public class InvalidNicknameException extends MemberException {
    public InvalidNicknameException(final String value) {
        super("닉네임이 올바르지 않습니다. 입력한 닉네임: " + value, HttpStatus.BAD_REQUEST);
    }
}
