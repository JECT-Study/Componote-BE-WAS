package ject.componote.infra.mail.application;

import ject.componote.infra.mail.dto.VerificationCodeMailRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendVerificationMail(final VerificationCodeMailRequest request) {
        log.info("이메일 전송 요청: {}", request);
        rabbitTemplate.convertAndSend("was-mail", "mail.verify", request);
    }
}
