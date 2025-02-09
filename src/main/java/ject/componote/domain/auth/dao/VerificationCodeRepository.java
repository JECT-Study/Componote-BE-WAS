package ject.componote.domain.auth.dao;

import ject.componote.domain.auth.domain.VerificationCode;

import java.util.Optional;

public interface VerificationCodeRepository {
    void save(final String email, final VerificationCode verificationCode);
    void deleteByEmail(final String email);
    Optional<VerificationCode> findByEmail(final String email);
}
