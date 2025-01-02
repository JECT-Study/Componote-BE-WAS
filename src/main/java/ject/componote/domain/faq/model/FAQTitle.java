package ject.componote.domain.faq.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class FAQTitle {
    private final String value;

    private FAQTitle(final String value) {
        this.value = value;
    }

    public static FAQTitle from(final String value) {
        return new FAQTitle(value);
    }
}
