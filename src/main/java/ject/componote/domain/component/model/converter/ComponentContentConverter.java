package ject.componote.domain.component.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ject.componote.domain.component.model.ComponentContent;

@Converter
public class ComponentContentConverter implements AttributeConverter<ComponentContent, String> {
    @Override
    public String convertToDatabaseColumn(final ComponentContent attribute) {
        return attribute.getValue();
    }

    @Override
    public ComponentContent convertToEntityAttribute(final String dbData) {
        return ComponentContent.from(dbData);
    }
}
