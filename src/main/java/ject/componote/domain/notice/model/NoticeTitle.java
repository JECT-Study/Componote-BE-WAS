package ject.componote.domain.notice.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@ToString
public class NoticeTitle {
    private final String value;

    private NoticeTitle(final String value) {
        this.value = value;
    }

    public static NoticeTitle from(final String value) {
        return new NoticeTitle(value);
    }
}
