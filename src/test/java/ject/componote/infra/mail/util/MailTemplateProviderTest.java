package ject.componote.infra.mail.util;

import ject.componote.infra.mail.model.VerificationCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.exceptions.TemplateProcessingException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MailTemplateProviderTest {
    @Mock
    TemplateEngine templateEngine;

    @InjectMocks
    MailTemplateProvider mailTemplateProvider;

    @Test
    @DisplayName("이메일 인증 코드 템플릿 생성")
    void createVerificationCodeTemplate() {
        // given
        final VerificationCode verificationCode = new VerificationCode("123456", LocalDateTime.now().plusMinutes(5));
        final String expect = "<html><body>Your Code: 123456</body></html>";
        doReturn(expect).when(templateEngine)
                .process(eq("email-verification"), any(Context.class));

        // when
        final String actual = mailTemplateProvider.createVerificationCodeTemplate(verificationCode);

        // then
        assertThat(expect).isEqualTo(actual);
        verify(templateEngine).process(eq("email-verification"), any(Context.class));
    }

    @Test
    @DisplayName("이메일 인증 코드 템플릿에 인증 코드가 포함되는지 확인")
    void shouldContainVerificationCode() {
        // given
        final VerificationCode verificationCode = new VerificationCode("123456", LocalDateTime.now().plusMinutes(5));
        final String expect = "<html><body>Your Code: 123456</body></html>";

        doReturn(expect).when(templateEngine)
                .process(eq("email-verification"), any(Context.class));

        // when
        final String actual = mailTemplateProvider.createVerificationCodeTemplate(verificationCode);

        // then
        assertThat(actual.contains("123456")).isTrue();
    }

    @Test
    @DisplayName("템플릿 생성 실패 시 예외 처리")
    void createVerificationCodeTemplateWithException() {
        // given
        final VerificationCode verificationCode = new VerificationCode("123456", LocalDateTime.now().plusMinutes(5));
        doThrow(new TemplateProcessingException("Template Error")).when(templateEngine)
                .process(anyString(), any(Context.class));

        // when & then
        assertThatThrownBy(
                () -> mailTemplateProvider.createVerificationCodeTemplate(verificationCode)
        ).isInstanceOf(TemplateProcessingException.class);
    }
}