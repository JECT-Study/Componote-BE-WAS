package ject.componote.domain.announcement.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ject.componote.domain.announcement.model.Title;

@Converter
public class TitleConverter implements AttributeConverter<Title, String> {
    @Override
    public String convertToDatabaseColumn(final Title attribute) {
        return attribute.getValue();
    }

    @Override
    public Title convertToEntityAttribute(final String dbData) {
        return Title.from(dbData);
    }
}
