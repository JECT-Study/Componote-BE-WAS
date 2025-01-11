package ject.componote.infra.mail.application;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import ject.componote.infra.mail.error.EmailMessageCreationException;
import ject.componote.infra.mail.error.NotFoundVerificationCodeException;
import ject.componote.infra.mail.model.VerificationCode;
import ject.componote.infra.mail.repository.VerificationCodeRepository;
import ject.componote.infra.mail.util.MailTemplateProvider;
import ject.componote.infra.mail.util.VerificationCodeProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {
    private static final String EMAIL_SENDING_FAIL_LOG_FORMAT = "이메일 인증 코드 발송 실패. [입력한 이메일]: {}, [메시지]: {}";

    private final ExecutorService mailExecutor;
    private final JavaMailSender mailSender;
    private final MailTemplateProvider mailTemplateProvider;
    private final VerificationCodeProvider verificationCodeProvider;
    private final VerificationCodeRepository verificationCodeRepository;

    public void sendVerificationCode(final String receiverEmail) {
        final VerificationCode verificationCode = verificationCodeProvider.createVerificationCode();
        final String htmlContent = mailTemplateProvider.createVerificationCodeTemplate(verificationCode.value());
        final MimeMessage message = createMimeMessage(receiverEmail, htmlContent);

        CompletableFuture.runAsync(() -> {
            mailSender.send(message);
            verificationCodeRepository.save(receiverEmail, verificationCode);
        }, mailExecutor).exceptionally(throwable -> {
            verificationCodeRepository.deleteByEmail(receiverEmail);
            log.error(EMAIL_SENDING_FAIL_LOG_FORMAT, receiverEmail, throwable.getMessage());
            return null;
        });
    }

    public void verifyEmailCode(final String email, final String codeValue) {
        final VerificationCode verificationCode = findVerificationCodeByEmail(email);
        if (!verificationCode.equalsValue(codeValue)) {
            throw new NotFoundVerificationCodeException();
        }
        verificationCodeRepository.deleteByEmail(email);
    }

    private MimeMessage createMimeMessage(final String receiverEmail, final String htmlContent) {
        final MimeMessage message = mailSender.createMimeMessage();
        try {
            final MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(receiverEmail);
            helper.setText(htmlContent, true);
            return message;
        } catch (MessagingException e) {
            throw new EmailMessageCreationException(e);
        }
    }

    private VerificationCode findVerificationCodeByEmail(final String email) {
        return verificationCodeRepository.findByEmail(email)
                .orElseThrow(NotFoundVerificationCodeException::new);
    }
}
