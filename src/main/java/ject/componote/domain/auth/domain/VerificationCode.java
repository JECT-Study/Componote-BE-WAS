package ject.componote.domain.auth.domain;

import java.time.LocalDateTime;

public record VerificationCode(String value, LocalDateTime expiredAt) {
    public boolean equalsValue(final String value) {
        return this.value.equals(value);
    }

    public boolean isExpired() {
        return this.expiredAt.isBefore(LocalDateTime.now());
    }
}
