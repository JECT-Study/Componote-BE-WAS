package ject.componote.domain.comment.model;

import ject.componote.domain.comment.error.InvalidCommentImageExtensionException;
import ject.componote.domain.common.model.BaseImage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentImageTest {
    @Test
    @DisplayName("이미지 ObjectKey가 없는 경우 null 저장")
    public void isNullOrEmpty() throws Exception {
        final String objectKey = "";
        final CommentImage commentImage = CommentImage.from(objectKey);
        assertThat(commentImage).isNotNull();
        assertThat(commentImage.getImage()).isEqualTo(BaseImage.getEmptyInstance());
    }

    @ParameterizedTest
    @DisplayName("이미지 ObjectKey 확장자가 잘못된 경우 예외 발생")
    @ValueSource(strings = {"hello.jp", "hello.gf"})
    public void invalidExtension(final String objectKey) throws Exception {
        assertThatThrownBy(() -> CommentImage.from(objectKey))
                .isInstanceOf(InvalidCommentImageExtensionException.class);
    }
}