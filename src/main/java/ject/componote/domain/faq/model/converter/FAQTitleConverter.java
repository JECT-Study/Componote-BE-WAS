package ject.componote.domain.faq.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ject.componote.domain.faq.model.FAQTitle;

@Converter
public class FAQTitleConverter implements AttributeConverter<FAQTitle, String> {
    @Override
    public String convertToDatabaseColumn(final FAQTitle attribute) {
        return attribute.getValue();
    }

    @Override
    public FAQTitle convertToEntityAttribute(final String dbData) {
        return FAQTitle.from(dbData);
    }
}
