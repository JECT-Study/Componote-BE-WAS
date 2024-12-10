package ject.componote.domain.announcement.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@ToString
public class Description {
    private final String value;

    private Description(final String value) {
        this.value = value;
    }

    public static Description from(final String value) {
        return new Description(value);
    }
}
