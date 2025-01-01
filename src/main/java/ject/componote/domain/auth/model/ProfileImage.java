package ject.componote.domain.auth.model;

import ject.componote.domain.auth.error.InvalidProfileImageExtensionException;
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
public class ProfileImage {
    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png");
    private static final ProfileImage DEFAULT_PROFILE_IMAGE = ProfileImage.from("/profiles/default-profile-image.png");

    private final BaseImage image;

    private ProfileImage(final BaseImage image) {
        this.image = image;
    }

    public static ProfileImage from(final String objectKey) {
        if (objectKey == null || objectKey.isEmpty()) {
            return DEFAULT_PROFILE_IMAGE;
        }

        final String extension = StringUtils.getFilenameExtension(objectKey);
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(extension)) {
            throw new InvalidProfileImageExtensionException(extension);
        }

        return new ProfileImage(BaseImage.from(objectKey));
    }
}
