package ject.componote.domain.member.model;

import ject.componote.domain.member.domain.BadWordFilteringSingleton;
import ject.componote.domain.member.error.InvalidNicknameException;
import ject.componote.domain.member.error.OffensiveNicknameException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@ToString
public class Nickname {
    private static final String NICKNAME_REGEX = "^(?!.*[ㄱ-ㅎㅏ-ㅣ])[A-Za-z0-9가-힣]{2,10}$";

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
            throw new InvalidNicknameException(value);
        }

        if (BadWordFilteringSingleton.containsBadWord(value)) {
            throw new OffensiveNicknameException(value);
        }
    }
}
