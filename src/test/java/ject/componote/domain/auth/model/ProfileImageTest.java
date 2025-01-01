package ject.componote.domain.auth.model;

import ject.componote.domain.auth.error.InvalidProfileImageExtensionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProfileImageTest {
    @ParameterizedTest(name = "값: {0}")
    @DisplayName("확장자가 잘못된 경우")
    @ValueSource(strings = {"hello.gif", "hello", "hello.jqp"})
    public void invalidExtension(final String objectKey) {
        assertThatThrownBy(() -> ProfileImage.from(objectKey))
                .isInstanceOf(InvalidProfileImageExtensionException.class);
    }

    @Test
    @DisplayName("objectKey가 전달되지 않으면 기본 프로필 objectKey로 생성")
    public void createDefault() {
        // given
        final String objectKey = null;

        // when
        final ProfileImage profileImage = ProfileImage.from(objectKey);

        // then
        assertThat(profileImage).isNotNull();
    }
}