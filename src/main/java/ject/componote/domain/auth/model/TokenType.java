package ject.componote.domain.auth.model;

import lombok.Getter;

@Getter
public enum TokenType {
    ACCESS,
    REFRESH,
    SOCIAL_ACCOUNT;

    public static TokenType from(final String name) {
        return valueOf(toConstantCase(name));
    }

    private static String toConstantCase(final String name) {
        return name.replace("-", "_")
                .toUpperCase();
    }
}
