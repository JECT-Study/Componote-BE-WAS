package ject.componote.domain.common.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class BaseImage {
    private static final String IMAGE_URL_PREFIX = "https://componote.s3.ap-northeast-2.amazonaws.com/permanent";
    private static final BaseImage EMPTY_INSTANCE = new BaseImage(null);

    private final String objectKey;

    public static BaseImage getEmptyInstance() {
        return EMPTY_INSTANCE;
    }

    private BaseImage(final String objectKey) {
        this.objectKey = objectKey;
    }

    public static BaseImage from(final String objectKey) {
        if (objectKey == null || objectKey.isEmpty()) {
            throw new IllegalArgumentException("The object key must not be null or empty");
        }

        return new BaseImage(objectKey);
    }

    public String toUrl() {
        if (isEmpty()) {
            return null;
        }

        return IMAGE_URL_PREFIX + objectKey;
    }

    public boolean isEmpty() {
        return objectKey == null || objectKey.isEmpty();
    }
}
