package ject.componote.domain.common.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ject.componote.domain.common.model.Count;

@Converter
public class CountConverter implements AttributeConverter<Count, Long> {
    @Override
    public Long convertToDatabaseColumn(final Count attribute) {
        return attribute.getValue();
    }

    @Override
    public Count convertToEntityAttribute(final Long dbData) {
        return Count.from(dbData);
    }
}
