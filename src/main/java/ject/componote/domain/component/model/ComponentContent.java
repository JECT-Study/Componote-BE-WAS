package ject.componote.domain.component.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class ComponentContent {
    private final String value;

    private ComponentContent(final String value) {
        this.value = value;
    }

    public static ComponentContent from(final String value) {
        return new ComponentContent(value);
    }
}
