package ject.componote.infra.mail.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class EmailMessageCreationException extends MailException {
    public EmailMessageCreationException(final Throwable throwable) {
        super("이메일 메시지 생성에 실패했습니다.", HttpStatus.BAD_REQUEST);
        log.warn("이메일 메시지 생성 실패: {}", throwable.getMessage());
    }
}
