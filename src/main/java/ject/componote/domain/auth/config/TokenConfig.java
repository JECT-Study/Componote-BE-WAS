package ject.componote.domain.auth.config;

import ject.componote.domain.auth.model.TokenAdapter;
import ject.componote.domain.auth.model.TokenProperties;
import ject.componote.domain.auth.model.InMemoryTokenProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TokenProperties.class)
@RequiredArgsConstructor
public class TokenConfig {
    private final TokenProperties properties;

    @Bean
    public InMemoryTokenProviderRepository inMemoryTokenProviderRepository() {
        return new InMemoryTokenProviderRepository(
                TokenAdapter.getTokenProviders(properties)
        );
    }
}
