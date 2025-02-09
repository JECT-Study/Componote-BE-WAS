package ject.componote.infra.mail.repository.impl;

import ject.componote.domain.auth.dao.impl.InMemoryVerificationCodeRepository;
import ject.componote.domain.auth.domain.VerificationCode;
import ject.componote.domain.auth.dao.VerificationCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryVerificationCodeRepositoryTest {
    VerificationCodeRepository verificationCodeRepository;

    @BeforeEach
    void setUp() {
        verificationCodeRepository = new InMemoryVerificationCodeRepository();
    }

    @Test
    @DisplayName("인증 코드 저장")
    public void save() throws Exception {
        // given
        final String email = "hello@gmail.com";
        final VerificationCode verificationCode = new VerificationCode("123456", LocalDateTime.now().plusMinutes(5));

        // when
        verificationCodeRepository.save(email, verificationCode);

        // then
        final Optional<VerificationCode> result = verificationCodeRepository.findByEmail(email);
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(verificationCode);
    }

    @Test
    @DisplayName("인증 코드 삭제")
    public void delete() throws Exception {
        // given
        final String email = "hello@gmail.com";
        final VerificationCode verificationCode = new VerificationCode("123456", LocalDateTime.now().plusMinutes(5));

        // when
        verificationCodeRepository.save(email, verificationCode);
        verificationCodeRepository.deleteByEmail(email);

        // then
        final Optional<VerificationCode> result = verificationCodeRepository.findByEmail(email);
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("이메일에 해당하는 인증 코드가 없는 경우 Optional.empty() 반환")
    public void findByEmailWhenInvalidEmail() throws Exception {
        // when
        final Optional<VerificationCode> result = verificationCodeRepository.findByEmail("hello@gmail.com");

        // then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("이메일에 해당하는 인증 코드가 만료된 경우 Optional.empty() 반환")
    public void findByEmailWhenExpired() throws Exception {
        // given
        final String email = "hello@gmail.com";
        final VerificationCode verificationCode = new VerificationCode("123456", LocalDateTime.now().minusMinutes(5));

        // when
        verificationCodeRepository.save(email, verificationCode);
        final Optional<VerificationCode> result = verificationCodeRepository.findByEmail("hello@gmail.com");

        // then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("여러 개의 인증 코드 저장")
    void shouldStoreMultipleVerificationCodes() {
        // given & when
        final int size = 10;
        final String[] emails = new String[size];
        final VerificationCode[] verificationCodes = new VerificationCode[size];
        for (int i = 0; i < size; i++) {
            emails[i] = "hello" + i + "@gmail.com";
            verificationCodes[i] = new VerificationCode("123456", LocalDateTime.now().plusMinutes(5));
            verificationCodeRepository.save(emails[i], verificationCodes[i]);
        }

        // then
        for (int i = 0; i < size; i++) {
            final Optional<VerificationCode> result = verificationCodeRepository.findByEmail(emails[i]);
            assertThat(result.isPresent()).isTrue();
            assertThat(result.get()).isEqualTo(verificationCodes[i]);
        }
    }

    @Test
    @DisplayName("동시성 테스트 (ConcurrentHashMap)")
    void handleConcurrency() throws InterruptedException {
        // given
        final String email = "concurrent@example.com";
        final VerificationCode verificationCode = new VerificationCode("123456", LocalDateTime.now().plusMinutes(5));

        // when
        final Runnable task = () -> verificationCodeRepository.save(email, verificationCode);
        final Thread thread1 = new Thread(task);
        final Thread thread2 = new Thread(task);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        // then
        final Optional<VerificationCode> result = verificationCodeRepository.findByEmail(email);
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(verificationCode);
    }
}