package ject.componote.domain.comment.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ject.componote.domain.comment.model.CommentContent;

@Converter
public class CommentContentConverter implements AttributeConverter<CommentContent, String> {
    @Override
    public String convertToDatabaseColumn(final CommentContent attribute) {
        return attribute.getValue();
    }

    @Override
    public CommentContent convertToEntityAttribute(final String dbData) {
        return CommentContent.from(dbData);
    }
}
