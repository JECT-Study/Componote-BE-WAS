package ject.componote.domain.design.model;

import ject.componote.domain.common.model.AbstractImage;
import ject.componote.domain.design.error.InvalidDesignPhotoImageExtensionException;
import ject.componote.domain.design.error.NotFoundDesignPhotoImageException;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ToString
public class DesignPhotoImage extends AbstractImage {
    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList("png");

    public DesignPhotoImage(final String objectKey) {
        super(objectKey);
    }

    public static DesignPhotoImage from(final String objectKey) {
        if (objectKey == null || objectKey.isEmpty()) {
            throw new NotFoundDesignPhotoImageException();
        }

        final String extension = StringUtils.getFilenameExtension(objectKey);
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(extension)) {
            throw new InvalidDesignPhotoImageExtensionException(extension);
        }

        return new DesignPhotoImage(objectKey);
    }
}
