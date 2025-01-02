package ject.componote.domain.component.model;

import ject.componote.domain.common.model.AbstractImage;
import ject.componote.domain.component.error.InvalidComponentThumbnailExtensionException;
import ject.componote.domain.component.error.NotFoundComponentThumbnailException;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ToString
public class ComponentThumbnail extends AbstractImage {
    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png");

    public ComponentThumbnail(final String objectKey) {
        super(objectKey);
    }

    public static ComponentThumbnail from(final String objectKey) {
        if (objectKey == null || objectKey.isEmpty()) {
            throw new NotFoundComponentThumbnailException();
        }

        final String extension = StringUtils.getFilenameExtension(objectKey);
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(extension)) {
            throw new InvalidComponentThumbnailExtensionException(extension);
        }

        return new ComponentThumbnail(objectKey);
    }
}
