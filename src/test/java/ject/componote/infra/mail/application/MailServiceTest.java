package ject.componote.infra.mail.application;

import ject.componote.infra.mail.error.NotFoundVerificationCodeException;
import ject.componote.infra.mail.model.VerificationCode;
import ject.componote.infra.mail.repository.VerificationCodeRepository;
import ject.componote.infra.mail.util.MailTemplateProvider;
import ject.componote.infra.mail.util.VerificationCodeProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {
    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MailTemplateProvider mailTemplateProvider;

    @Mock
    private VerificationCodeProvider verificationCodeProvider;

    @Mock
    private VerificationCodeRepository verificationCodeRepository;

    @Mock
    private ExecutorService mailExecutor;

    @InjectMocks
    private MailService mailService;

    @BeforeEach
    public void setUp() {
        mailExecutor = Executors.newFixedThreadPool(10);
    }

    @AfterEach
    void shutdown() {
        mailExecutor.shutdown();
    }

//    @Test
//    @DisplayName("이메일 인증 코드 전송")
//    void sendVerificationCode() {
//        // given
//        final String receiverEmail = "test@example.com";
//        final VerificationCode verificationCode = new VerificationCode("123456", LocalDateTime.now().plusMinutes(5));
//        final MimeMessage message = mock(MimeMessage.class);
//
//        doReturn(verificationCode).when(verificationCodeProvider)
//                .createVerificationCode();
//        doReturn("<html>Email Template</html>").when(mailTemplateProvider)
//                .createVerificationCodeTemplate(verificationCode);
//        doReturn(message).when(mailSender)
//                .createMimeMessage();
//        doNothing().when(mailSender)
//                .send(message);
//        doNothing().when(verificationCodeRepository)
//                .save(receiverEmail, verificationCode);
//
//        // when
//        mailService.sendVerificationCode(receiverEmail);
//
//        // then
//        await().atMost(5, TimeUnit.SECONDS)
//                .untilAsserted(
//                        () -> {
//                            verify(mailSender, timeout(1000)).send(any(MimeMessage.class));
//                            verify(verificationCodeRepository, timeout(1000)).save(eq(receiverEmail), eq(verificationCode));
//                        }
//                );
//    }

//    @Test
//    @DisplayName("이메일 전송 실패 시 예외 발생")
//    void sendVerificationCodeWhenEmailSendingFail() {
//        // given
//        final String receiverEmail = "test@example.com";
//        final VerificationCode verificationCode = new VerificationCode("123456", LocalDateTime.now().plusMinutes(5));
//        final MimeMessage message = mock(MimeMessage.class);
//
//        doReturn(verificationCode).when(verificationCodeProvider)
//                .createVerificationCode();
//        doReturn("<html>Email Template</html>").when(mailTemplateProvider)
//                .createVerificationCodeTemplate(verificationCode);
//        doReturn(message).when(mailSender)
//                .createMimeMessage();
//        doThrow(new MailSendException("Failed to send")).when(mailSender)
//                .send(any(MimeMessage.class));
//        doNothing().when(verificationCodeRepository)
//                .deleteByEmail(receiverEmail);
//
//        // when
//        mailService.sendVerificationCode(receiverEmail);
//
//        // then
//        await().atMost(2, TimeUnit.SECONDS)
//                .untilAsserted(
//                        () -> verify(verificationCodeRepository).deleteByEmail(receiverEmail)
//                );
//    }

    @Test
    @DisplayName("인증 코드 검증")
    void verifyVerificationCode() {
        // given
        final String receiverEmail = "test@example.com";
        final VerificationCode verificationCode = new VerificationCode("123456", LocalDateTime.now().plusMinutes(5));

        doReturn(Optional.of(verificationCode)).when(verificationCodeRepository)
                .findByEmail(receiverEmail);

        // when
        mailService.verifyEmailCode(receiverEmail, "123456");

        // then
        verify(verificationCodeRepository).deleteByEmail(receiverEmail);
    }

    @Test
    @DisplayName("인증 코드가 잘못된 경우 예외 발생")
    void verifyVerificationCodeWhenInvalidCode() {
        // given
        final String receiverEmail = "test@example.com";
        final VerificationCode verificationCode = new VerificationCode("123456", LocalDateTime.now().plusMinutes(5));

        doReturn(Optional.of(verificationCode)).when(verificationCodeRepository)
                .findByEmail(receiverEmail);

        // when & then
        assertThatThrownBy(
                () -> mailService.verifyEmailCode(receiverEmail, "wrongcode")
        ).isInstanceOf(NotFoundVerificationCodeException.class);
    }

    @Test
    @DisplayName("존재하지 않는 인증 코드의 경우 예외 발생")
    void verifyVerificationCodeWhenNotExistsCode() {
        // given
        final String receiverEmail = "test@example.com";
        doReturn(Optional.empty()).when(verificationCodeRepository)
                .findByEmail(receiverEmail);

        // when & then
        assertThatThrownBy(
                () -> mailService.verifyEmailCode(receiverEmail, "123456")
        ).isInstanceOf(NotFoundVerificationCodeException.class);
    }
}