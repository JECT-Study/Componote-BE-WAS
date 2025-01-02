package ject.componote.domain.component.model;

import ject.componote.domain.common.model.BaseImage;
import ject.componote.domain.component.error.InvalidComponentImageExtensionException;
import ject.componote.domain.component.error.NotFoundComponentImageException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode
@Getter
public class ComponentImage {
    // Arrays.asList 로 만든 List: contains(null) 시 NPE 발생하지 않고 false 리턴
    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList("png");

    private final BaseImage image;

    private ComponentImage(final BaseImage image) {
        this.image = image;
    }

    public static ComponentImage from(final String objectKey) {
        if (objectKey == null || objectKey.isEmpty()) {
            throw new NotFoundComponentImageException();
        }

        final String extension = StringUtils.getFilenameExtension(objectKey);
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(extension)) {
            throw new InvalidComponentImageExtensionException(extension);
        }

        return new ComponentImage(BaseImage.from(objectKey));
    }
}
