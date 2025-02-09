package ject.componote.domain.design.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ject.componote.domain.design.model.Url;

@Converter
public class UrlConverter implements AttributeConverter<Url, String> {
    @Override
    public String convertToDatabaseColumn(final Url url) {
        return url.getValue();
    }

    @Override
    public Url convertToEntityAttribute(final String s) {
        return Url.from(s);
    }
}
