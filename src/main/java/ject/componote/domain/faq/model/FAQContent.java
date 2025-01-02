package ject.componote.domain.faq.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@ToString
public class FAQContent {
    private final String value;

    private FAQContent(final String value) {
        this.value = value;
    }

    public static FAQContent from(final String value) {
        return new FAQContent(value);
    }
}
