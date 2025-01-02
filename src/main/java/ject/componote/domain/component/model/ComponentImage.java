package ject.componote.domain.component.model;

import ject.componote.domain.common.model.AbstractImage;
import ject.componote.domain.component.error.InvalidComponentImageExtensionException;
import ject.componote.domain.component.error.NotFoundComponentImageException;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ToString
public class ComponentImage extends AbstractImage {
    // Arrays.asList 로 만든 List: contains(null) 시 NPE 발생하지 않고 false 리턴
    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList("png");

    public ComponentImage(final String objectKey) {
        super(objectKey);
    }

    public static ComponentImage from(final String objectKey) {
        if (objectKey == null || objectKey.isEmpty()) {
            throw new NotFoundComponentImageException();
        }

        final String extension = StringUtils.getFilenameExtension(objectKey);
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(extension)) {
            throw new InvalidComponentImageExtensionException(extension);
        }

        return new ComponentImage(objectKey);
    }
}
