package ject.componote.domain.auth.model;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class InMemoryTokenProviderRepository {
    private final Map<TokenType, TokenProvider> providers;

    public TokenProvider getProvider(final TokenType type) {
        return providers.get(type);
    }
}
