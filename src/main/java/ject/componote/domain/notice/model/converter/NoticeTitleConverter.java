package ject.componote.domain.notice.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ject.componote.domain.notice.model.NoticeTitle;

@Converter
public class NoticeTitleConverter implements AttributeConverter<NoticeTitle, String> {
    @Override
    public String convertToDatabaseColumn(final NoticeTitle attribute) {
        return attribute.getValue();
    }

    @Override
    public NoticeTitle convertToEntityAttribute(final String dbData) {
        return NoticeTitle.from(dbData);
    }
}
