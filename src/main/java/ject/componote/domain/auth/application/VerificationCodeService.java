package ject.componote.domain.auth.application;

import ject.componote.domain.auth.dao.VerificationCodeRepository;
import ject.componote.domain.auth.domain.VerificationCode;
import ject.componote.domain.auth.error.NotFoundVerificationCodeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {
    private final VerificationCodeRepository verificationCodeRepository;

    public void verifyEmailCode(final String email, final String codeValue) {
        final VerificationCode verificationCode = findVerificationCodeByEmail(email);
        if (!verificationCode.equalsValue(codeValue)) {
            throw new NotFoundVerificationCodeException();
        }
        verificationCodeRepository.deleteByEmail(email);
    }

    private VerificationCode findVerificationCodeByEmail(final String email) {
        return verificationCodeRepository.findByEmail(email)
                .orElseThrow(NotFoundVerificationCodeException::new);
    }
}
