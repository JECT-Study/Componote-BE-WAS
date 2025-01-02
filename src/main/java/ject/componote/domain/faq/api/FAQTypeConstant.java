package ject.componote.domain.faq.api;

import ject.componote.domain.faq.domain.FAQType;
import lombok.Getter;

@Getter
public enum FAQTypeConstant {
    ALL(null),
    COMPONENT(FAQType.COMPONENT),
    DESIGN(FAQType.DESIGN),
    SERVICE(FAQType.SERVICE),
    ETC(FAQType.ETC);

    private final FAQType type;

    FAQTypeConstant(final FAQType type) {
        this.type = type;
    }

    public boolean isAll() {
        return this == ALL;
    }
}
