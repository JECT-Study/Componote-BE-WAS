package ject.componote.domain.common.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ject.componote.domain.common.model.BaseImage;

@Converter
public class BaseImageConverter implements AttributeConverter<BaseImage, String> {
    @Override
    public String convertToDatabaseColumn(final BaseImage attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.getObjectKey();
    }

    @Override
    public BaseImage convertToEntityAttribute(final String dbData) {
        return BaseImage.from(dbData);
    }
}
