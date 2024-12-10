package ject.componote.domain.comment.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class CommentContent {
    private final String value;

    private CommentContent(final String value) {
        validateContent(value);
        this.value = value;
    }

    public static CommentContent from(final String value) {
        return new CommentContent(value);
    }

    private void validateContent(final String value) {
        // 추가 제약조건 고려 (e.g. 글자수, 비속어 등)
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Comment content cannot be null or blank");
        }
    }
}
