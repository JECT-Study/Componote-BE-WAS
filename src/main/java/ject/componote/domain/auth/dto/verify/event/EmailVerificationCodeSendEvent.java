package ject.componote.domain.auth.dto.verify.event;

import ject.componote.domain.auth.model.Email;

public record EmailVerificationCodeSendEvent(String email) {
    public static EmailVerificationCodeSendEvent from(final Email email) {
        return new EmailVerificationCodeSendEvent(email.getValue());
    }
}
