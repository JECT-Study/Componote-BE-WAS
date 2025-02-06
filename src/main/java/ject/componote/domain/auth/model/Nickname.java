package ject.componote.domain.auth.model;

import ject.componote.domain.auth.domain.BadWordFilteringSingleton;
import ject.componote.domain.auth.error.InvalidNicknameLengthException;
import ject.componote.domain.auth.error.InvalidNicknameCharacterException;
import ject.componote.domain.auth.error.OffensiveNicknameException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@ToString
public class Nickname {
    // 예외 처리때문에 아래 상수들은 public 으로 선언
    public static final int MIN_LENGTH = 2;
    public static final int MAX_LENGTH = 10;

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
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new InvalidNicknameLengthException();
        }

        if (!value.matches(NICKNAME_REGEX)) {
            throw new InvalidNicknameCharacterException();
        }

        if (BadWordFilteringSingleton.containsBadWord(value)) {
            throw new OffensiveNicknameException();
        }
    }
}
