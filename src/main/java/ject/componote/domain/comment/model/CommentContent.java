package ject.componote.domain.comment.model;

import ject.componote.domain.auth.domain.BadWordFilteringSingleton;
import ject.componote.domain.comment.error.BlankCommentException;
import ject.componote.domain.comment.error.ExceededCommentLengthException;
import ject.componote.domain.comment.error.OffensiveCommentException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class CommentContent {
    private static final int MAX_LENGTH = 1_000;

    private final String value;

    private CommentContent(final String value) {
        validateContent(value);
        this.value = value;
    }

    public static CommentContent from(final String value) {
        return new CommentContent(value);
    }

    private void validateContent(final String value) {
        if (value == null || value.isBlank()) {
            throw new BlankCommentException();
        }

        if (value.length() > MAX_LENGTH) {
            throw new ExceededCommentLengthException(value.length());
        }

        if (BadWordFilteringSingleton.containsBadWord(value)) {
            throw new OffensiveCommentException();
        }
    }
}
