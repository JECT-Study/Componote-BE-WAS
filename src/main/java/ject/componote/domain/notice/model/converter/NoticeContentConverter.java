package ject.componote.domain.notice.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ject.componote.domain.notice.model.NoticeContent;

@Converter
public class NoticeContentConverter implements AttributeConverter<NoticeContent, String> {
    @Override
    public String convertToDatabaseColumn(final NoticeContent attribute) {
        return attribute.getValue();
    }

    @Override
    public NoticeContent convertToEntityAttribute(final String dbData) {
        return NoticeContent.from(dbData);
    }
}
