package ject.componote.domain.common.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class Image {
    private final String filename;  // 변경해도 상관없음

    private Image(final String filename) {
        this.filename = filename;
    }

    public static Image from(final String filename) {
        return new Image(filename);
    }

    private void validateFileName(final String filename) {
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be null or empty");
        }
    }

    public String toPath(final String folder) {
        return folder + "/" + filename;
    }

    public String toUrl(final String bucketName, final String folder) {
        return "https://" + bucketName + ".s3.amazonaws.com" + toPath(folder);
    }
}
