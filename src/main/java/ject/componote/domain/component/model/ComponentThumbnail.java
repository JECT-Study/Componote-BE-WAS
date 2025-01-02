package ject.componote.domain.component.model;

import ject.componote.domain.common.model.BaseImage;
import ject.componote.domain.component.error.InvalidComponentThumbnailExtensionException;
import ject.componote.domain.component.error.NotFoundComponentThumbnailException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode
@Getter
public class ComponentThumbnail {
    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png");

    private final BaseImage image;

    private ComponentThumbnail(final BaseImage image) {
        this.image = image;
    }

    public static ComponentThumbnail from(final String objectKey) {
        if (objectKey == null || objectKey.isEmpty()) {
            throw new NotFoundComponentThumbnailException();
        }

        final String extension = StringUtils.getFilenameExtension(objectKey);
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(extension)) {
            throw new InvalidComponentThumbnailExtensionException(extension);
        }

        return new ComponentThumbnail(BaseImage.from(objectKey));
    }
}
