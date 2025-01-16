package ject.componote.infra.mail.util;

import ject.componote.infra.mail.model.VerificationCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class MailTemplateProvider {
    private static final String VERIFICATION_CODE_KEY = "verificationCode";
    private static final String EMAIL_VERIFICATION_TEMPLATE = "email-verification";

    private final TemplateEngine templateEngine;

    public String createVerificationCodeTemplate(final VerificationCode verificationCode) {
        final Context context = new Context();  // Thread-Safe 하지 않아 매 요청마다 객체 생성
        context.setVariable(VERIFICATION_CODE_KEY, verificationCode.value());
        return templateEngine.process(EMAIL_VERIFICATION_TEMPLATE, context);
    }
}
