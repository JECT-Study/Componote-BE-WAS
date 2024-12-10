package ject.componote.domain.common.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class Count {
    private Long value;

    private Count(final Long value) {
        validateCount(value);
        this.value = value;
    }

    public static Count from(final Long value) {
        return new Count(value);
    }

    public static Count create() {
        return new Count(0L);
    }

    public void increase() {
        this.value++;
    }

    public void decrease() {
        this.value--;
    }

    private void validateCount(final Long value) {
        if (value < 0) {
            throw new IllegalArgumentException("Value must be greater than zero");
        }
    }
}
