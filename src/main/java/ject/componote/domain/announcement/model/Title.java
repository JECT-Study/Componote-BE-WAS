package ject.componote.domain.announcement.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@ToString
public class Title {
    private final String value;

    private Title(final String value) {
        this.value = value;
    }

    public static Title from(final String value) {
        return new Title(value);
    }
}
