package ject.componote.infra.mail.util;

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

    public String createVerificationCodeTemplate(final String verificationCode) {
        final Context context = new Context();
        context.setVariable(VERIFICATION_CODE_KEY, verificationCode);
        return templateEngine.process(EMAIL_VERIFICATION_TEMPLATE, context);
    }
}
