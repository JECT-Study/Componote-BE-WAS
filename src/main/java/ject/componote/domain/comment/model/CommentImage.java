package ject.componote.domain.comment.model;

import ject.componote.domain.comment.error.InvalidCommentImageExtensionException;
import ject.componote.domain.common.model.BaseImage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode
@Getter
@ToString
public class CommentImage {
    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");

    private final BaseImage image;

    private CommentImage(final BaseImage image) {
        this.image = image;
    }

    public static CommentImage from(final String objectKey) {
        if (objectKey == null || objectKey.isEmpty()) {
            return new CommentImage(BaseImage.getEmptyInstance());
        }

        final String extension = StringUtils.getFilenameExtension(objectKey);
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(extension)) {
            throw new InvalidCommentImageExtensionException(extension);
        }

        return new CommentImage(BaseImage.from(objectKey));
    }
}
