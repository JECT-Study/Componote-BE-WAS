package ject.componote.domain.member.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ject.componote.domain.member.model.Nickname;

@Converter
public class NicknameConverter implements AttributeConverter<Nickname, String> {
    @Override
    public String convertToDatabaseColumn(final Nickname attribute) {
        return attribute.getValue();
    }

    @Override
    public Nickname convertToEntityAttribute(final String dbData) {
        return Nickname.from(dbData);
    }
}
