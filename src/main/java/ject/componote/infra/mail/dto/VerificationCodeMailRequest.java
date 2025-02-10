package ject.componote.infra.mail.dto;

import ject.componote.domain.auth.domain.VerificationCode;

public record VerificationCodeMailRequest(String email, String code) {
    public static VerificationCodeMailRequest of(final String email, VerificationCode verificationCode) {
        return new VerificationCodeMailRequest(email, verificationCode.value());
    }
}
