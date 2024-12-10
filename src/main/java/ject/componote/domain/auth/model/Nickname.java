package ject.componote.domain.auth.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@ToString
public class Nickname {
    private static final String NICKNAME_REGEX = "^(?![-_]{2,10}$)[가-힣ㄱ-ㅎㅏ-ㅣA-Za-z0-9_-]{2,10}$";

    private final String value;

    private Nickname(final String value) {
        validateNickname(value);
        this.value = value;
    }

    public static Nickname from(final String value) {
        return new Nickname(value);
    }

    private void validateNickname(final String value) {
        if (!value.matches(NICKNAME_REGEX)) {
            throw new IllegalArgumentException("Invalid nickname: " + value);
        }
    }
}
