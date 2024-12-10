package ject.componote.domain.announcement.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ject.componote.domain.announcement.model.Description;

@Converter
public class DescriptionConverter implements AttributeConverter<Description, String> {
    @Override
    public String convertToDatabaseColumn(final Description attribute) {
        return attribute.getValue();
    }

    @Override
    public Description convertToEntityAttribute(final String dbData) {
        return Description.from(dbData);
    }
}
