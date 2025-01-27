package ject.componote.domain.member.error;

import ject.componote.domain.auth.error.AuthException;
import org.springframework.http.HttpStatus;

public class NotFoundMemberException extends AuthException {
    private NotFoundMemberException(final String message, final HttpStatus status) {
        super(message, status);
    }

    public static NotFoundMemberException createWhenInvalidMemberId(final Long memberId) {
        return new NotFoundMemberException("일치하는 회원을 찾을 수 없습니다. 회원 ID: " + memberId, HttpStatus.NOT_FOUND);
    }

    public static NotFoundMemberException createWhenInvalidSocialAccountId() {
        return new NotFoundMemberException("소셜 ID에 해당하는 회원이 없습니다.", HttpStatus.NOT_FOUND);
    }
}
