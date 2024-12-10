package ject.componote.domain.auth.domain;

import java.util.Arrays;

public enum ProviderType {
    GOOGLE, GITHUB;

    public static ProviderType from(final String providerTypeValue) {
        return Arrays.stream(values())
                .filter(value -> providerTypeValue.toUpperCase().equals(value.name()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Unknown provider type: " + providerTypeValue));
    }
}
