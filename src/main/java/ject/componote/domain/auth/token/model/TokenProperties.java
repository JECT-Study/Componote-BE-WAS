package ject.componote.domain.auth.token.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "auth")
@Getter
@ToString
public class TokenProperties {
    private final Map<String, Token> token = new HashMap<>();

    public Token getAttributeFrom(final String key) {
        return token.get(key);
    }

    @Getter
    @Setter
    @ToString
    public static class Token {
        private Long expiration;
        private String secretKey;
    }
}
