package ject.componote.domain.auth.error;

import org.springframework.http.HttpStatus;

public class NotFoundMemberException extends AuthException {
    public NotFoundMemberException(final Long memberId) {
        super("일치하는 회원을 찾을 수 없습니다. 회원 ID: " + memberId, HttpStatus.NOT_FOUND);
    }
}
