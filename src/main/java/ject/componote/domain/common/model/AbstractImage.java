package ject.componote.domain.common.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@ToString
public abstract class AbstractImage {
    private static final String IMAGE_URL_PREFIX = "https://componote.s3.ap-northeast-2.amazonaws.com/data/";

    private final String objectKey;

    protected AbstractImage(final String objectKey) {
        this.objectKey = objectKey;
    }

    public boolean isEmpty() {
        return objectKey == null || objectKey.isEmpty();
    }

    public String toUrl() {
        if (isEmpty()) {
            return null;
        }

        return IMAGE_URL_PREFIX + objectKey;
    }
}
