package ject.componote.domain.faq.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ject.componote.domain.faq.model.FAQContent;

@Converter
public class FAQContentConverter implements AttributeConverter<FAQContent, String> {
    @Override
    public String convertToDatabaseColumn(final FAQContent attribute) {
        return attribute.getValue();
    }

    @Override
    public FAQContent convertToEntityAttribute(final String dbData) {
        return FAQContent.from(dbData);
    }
}
