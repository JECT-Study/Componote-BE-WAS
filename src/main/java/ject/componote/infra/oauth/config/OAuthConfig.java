package ject.componote.infra.oauth.config;

import ject.componote.infra.oauth.model.OAuthAdapter;
import ject.componote.infra.oauth.model.OAuthProperties;
import ject.componote.infra.oauth.model.OAuthProvider;
import ject.componote.infra.oauth.repository.InMemoryOAuthProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@EnableConfigurationProperties(OAuthProperties.class)
@RequiredArgsConstructor
public class OAuthConfig {
    private final OAuthProperties properties;

    @Bean
    public InMemoryOAuthProviderRepository inMemoryOAuthProviderRepository() {
        final Map<String, OAuthProvider> providers = OAuthAdapter.getOAuthProviders(properties);
        return new InMemoryOAuthProviderRepository(providers);
    }
}
