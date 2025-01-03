package ject.componote.domain.comment.application;

import ject.componote.domain.comment.domain.Comment;
import ject.componote.domain.comment.dto.create.request.CommentCreateRequest;
import ject.componote.fixture.CommentFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class CommentCreationStrategyTest {
    @ParameterizedTest
    @DisplayName("요청값에 알맞는 댓글 엔티티 생성")
    @EnumSource(CommentFixture.class)
    public void createBy(final CommentFixture fixture) throws Exception {
        // given
        final Comment comment = fixture.생성();
        final CommentCreateRequest createRequest = fixture.toCreateRequest();
        final Long memberId = comment.getMemberId();

        // when
        final Comment createdComment = CommentCreationStrategy.createBy(createRequest, memberId);

        // then
        assertThat(createdComment.getContent().getValue()).isEqualTo(createRequest.content());
        assertThat(createdComment.getImage().getObjectKey()).isEqualTo(createRequest.imageObjectKey());
        assertThat(createdComment.getMemberId()).isEqualTo(memberId);
        assertThat(createdComment.getComponentId()).isEqualTo(createRequest.componentId());
        assertThat(createdComment.getParentId()).isEqualTo(createRequest.parentId());
    }
}