package ject.componote.infra.mail.repository.impl;

import ject.componote.infra.mail.model.VerificationCode;
import ject.componote.infra.mail.repository.VerificationCodeRepository;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryVerificationCodeRepository implements VerificationCodeRepository {
    private final Map<String, VerificationCode> storage = new ConcurrentHashMap<>();

    @Override
    public void save(final String email, final VerificationCode verificationCode) {
        storage.put(email, verificationCode);
    }

    @Override
    public void deleteByEmail(final String email) {
        storage.remove(email);
    }

    @Override
    public Optional<VerificationCode> findByEmail(final String email) {
        if (!storage.containsKey(email)) {
            return Optional.empty();
        }

        final VerificationCode verificationCode = storage.get(email);
        if (verificationCode.isExpired()) {
            storage.remove(email);
            return Optional.empty();
        }

        return Optional.of(verificationCode);
    }
}
