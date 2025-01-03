package ject.componote.domain.auth.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ject.componote.domain.auth.model.ProfileImage;

@Converter
public class ProfileImageConverter implements AttributeConverter<ProfileImage, String> {
    @Override
    public String convertToDatabaseColumn(final ProfileImage attribute) {
        return attribute.getObjectKey();
    }

    @Override
    public ProfileImage convertToEntityAttribute(final String dbData) {
        return ProfileImage.from(dbData);
    }
}
