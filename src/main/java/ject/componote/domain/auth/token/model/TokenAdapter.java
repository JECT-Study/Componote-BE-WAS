package ject.componote.domain.auth.token.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenAdapter {
    public static Map<TokenType, TokenProvider> getTokenProviders(final TokenProperties properties) {
        return properties.getToken()
                .keySet()
                .stream()
                .collect(Collectors.toMap(
                        TokenType::from,
                        key -> TokenProvider.from(properties.getAttributeFrom(key))
                ));
    }
}
