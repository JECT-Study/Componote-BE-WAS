package ject.componote.infra.mail.repository;

import ject.componote.infra.mail.model.VerificationCode;

import java.util.Optional;

public interface VerificationCodeRepository {
    void save(final String email, final VerificationCode verificationCode);
    void deleteByEmail(final String email);
    Optional<VerificationCode> findByEmail(final String email);
}
