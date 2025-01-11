package ject.componote.infra.mail.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class MailConfig {
    /**
     * 인증 코드 전송 스레드 풀 빈 등록 메서드
     * @return 인증 코드 전송 스레드 풀
     */
    @Bean("mailExecutor")
    public ExecutorService mailExecutor() {
        return Executors.newFixedThreadPool(10);
    }
}
