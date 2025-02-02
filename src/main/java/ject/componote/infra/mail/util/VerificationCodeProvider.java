package ject.componote.infra.mail.util;

import ject.componote.infra.mail.model.VerificationCode;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class VerificationCodeProvider {
    private static final int VERIFICATION_CODE_LENGTH = 6;
    private static final long EXPIRATION_MINUTE = 5L;

    public VerificationCode createVerificationCode() {
        return new VerificationCode(
                createCode(),
                LocalDateTime.now().plusMinutes(EXPIRATION_MINUTE)
        );
    }

    private String createCode() {
        return UUID.randomUUID()
                .toString()
                .substring(0, VERIFICATION_CODE_LENGTH);
    }
}
