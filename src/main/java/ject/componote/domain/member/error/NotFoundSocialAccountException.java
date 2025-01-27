package ject.componote.domain.member.error;

import org.springframework.http.HttpStatus;

public class NotFoundSocialAccountException extends MemberException {
    public NotFoundSocialAccountException() {
        super("일치하는 소셜 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
