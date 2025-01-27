package ject.componote.domain.auth.model;

import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;

public record TokenProvider(Long expiration, Key key) {
    public static TokenProvider from(final TokenProperties.Token attribute) {
        return new TokenProvider(
                attribute.getExpiration(),
                Keys.hmacShaKeyFor(attribute.getSecretKey().getBytes(StandardCharsets.UTF_8))
        );
    }
}
