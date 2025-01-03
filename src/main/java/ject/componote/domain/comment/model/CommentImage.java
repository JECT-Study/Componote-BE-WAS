package ject.componote.domain.comment.model;

import ject.componote.domain.comment.error.InvalidCommentImageExtensionException;
import ject.componote.domain.common.model.AbstractImage;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ToString
public class CommentImage extends AbstractImage {
    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");
    private static final CommentImage EMPTY_INSTANCE = new CommentImage(null);

    public CommentImage(final String objectKey) {
        super(objectKey);
    }

    public static CommentImage from(final String objectKey) {
        if (objectKey == null || objectKey.isEmpty()) {
            return EMPTY_INSTANCE;
        }

        final String extension = StringUtils.getFilenameExtension(objectKey);
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(extension)) {
            throw new InvalidCommentImageExtensionException(extension);
        }

        return new CommentImage(objectKey);
    }
}
