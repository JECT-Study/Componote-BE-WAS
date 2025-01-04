package ject.componote.domain.design.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ject.componote.domain.design.model.DesignPhotoImage;

@Converter
public class DesignPhotoImageConverter implements AttributeConverter<DesignPhotoImage, String> {
    @Override
    public String convertToDatabaseColumn(final DesignPhotoImage attribute) {
        return attribute.getObjectKey();
    }

    @Override
    public DesignPhotoImage convertToEntityAttribute(final String dbData) {
        return DesignPhotoImage.from(dbData);
    }
}
