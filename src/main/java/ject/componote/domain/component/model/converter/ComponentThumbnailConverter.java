package ject.componote.domain.component.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ject.componote.domain.component.model.ComponentThumbnail;

@Converter
public class ComponentThumbnailConverter implements AttributeConverter<ComponentThumbnail, String> {
    @Override
    public String convertToDatabaseColumn(final ComponentThumbnail attribute) {
        return attribute.getObjectKey();
    }

    @Override
    public ComponentThumbnail convertToEntityAttribute(final String dbData) {
        return ComponentThumbnail.from(dbData);
    }
}
