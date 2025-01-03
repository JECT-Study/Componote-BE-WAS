package ject.componote.domain.notice.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@ToString
public class NoticeContent {
    private final String value;

    private NoticeContent(final String value) {
        this.value = value;
    }

    public static NoticeContent from(final String value) {
        return new NoticeContent(value);
    }
}
