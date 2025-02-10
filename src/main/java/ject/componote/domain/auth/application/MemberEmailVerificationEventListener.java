package ject.componote.domain.auth.application;

import ject.componote.domain.auth.dao.VerificationCodeRepository;
import ject.componote.domain.auth.domain.VerificationCode;
import ject.componote.domain.auth.dto.verify.event.EmailVerificationCodeSendEvent;
import ject.componote.domain.auth.util.VerificationCodeProvider;
import ject.componote.infra.mail.application.MailProducer;
import ject.componote.infra.mail.dto.VerificationCodeMailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberEmailVerificationEventListener {
    private final MailProducer mailProducer;
    private final VerificationCodeProvider verificationCodeProvider;
    private final VerificationCodeRepository verificationCodeRepository;

    @Async
    @EventListener
    public void handleVerificationCodeSendEvent(final EmailVerificationCodeSendEvent event) {
        final String email = event.email();
        if (email == null || email.isBlank()) {
            return;
        }
        final VerificationCode verificationCode = verificationCodeProvider.createVerificationCode();
        verificationCodeRepository.save(email, verificationCode);
        mailProducer.sendVerificationMail(VerificationCodeMailRequest.of(email, verificationCode));
    }
}
