package ject.componote.domain.comment.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ject.componote.domain.comment.model.CommentImage;

@Converter
public class CommentImageConverter implements AttributeConverter<CommentImage, String> {
    @Override
    public String convertToDatabaseColumn(final CommentImage attribute) {
        return attribute.getObjectKey();
    }

    @Override
    public CommentImage convertToEntityAttribute(final String dbData) {
        return CommentImage.from(dbData);
    }
}
