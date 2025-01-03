package ject.componote.domain.comment.model;

import ject.componote.domain.comment.error.BlankCommentException;
import ject.componote.domain.comment.error.ExceedCommentLengthException;
import ject.componote.domain.comment.error.OffensiveCommentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentContentTest {
    @Test
    @DisplayName("댓글 내용이 너무 긴 경우 예외 발생")
    public void exceedLength() throws Exception {
        final String value = "H".repeat(1_500);
        assertThatThrownBy(() -> CommentContent.from(value))
                .isInstanceOf(ExceedCommentLengthException.class);
    }

    @Test
    @DisplayName("댓글 내용이 없는 경우 예외 발생")
    public void isNullOrEmpty() throws Exception {
        final String value = "";
        assertThatThrownBy(() -> CommentContent.from(value))
                .isInstanceOf(BlankCommentException.class);
    }

    @ParameterizedTest
    @DisplayName("비속어 필터링")
    @ValueSource(strings = {"씨발", "개새끼"})
    public void badWordFiltering(final String value) throws Exception {
        assertThatThrownBy(() -> CommentContent.from(value))
                .isInstanceOf(OffensiveCommentException.class);
    }
}