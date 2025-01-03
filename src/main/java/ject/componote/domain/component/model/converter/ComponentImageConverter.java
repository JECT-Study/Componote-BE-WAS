package ject.componote.domain.component.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ject.componote.domain.component.model.ComponentImage;

@Converter
public class ComponentImageConverter implements AttributeConverter<ComponentImage, String> {
    @Override
    public String convertToDatabaseColumn(final ComponentImage attribute) {
        return attribute.getObjectKey();
    }

    @Override
    public ComponentImage convertToEntityAttribute(final String dbData) {
        return ComponentImage.from(dbData);
    }
}
