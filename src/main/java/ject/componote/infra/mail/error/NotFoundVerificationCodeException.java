package ject.componote.infra.mail.error;

import org.springframework.http.HttpStatus;

public class NotFoundVerificationCodeException extends MailException {
    public NotFoundVerificationCodeException() {
        super("인증 코드가 만료되었거나 잘못되었습니다.", HttpStatus.NOT_FOUND);
    }
}
