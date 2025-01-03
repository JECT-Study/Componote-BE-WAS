package ject.componote.domain.comment.application;

import ject.componote.domain.auth.domain.Member;
import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.comment.dao.CommentLikeRepository;
import ject.componote.domain.comment.dao.CommentRepository;
import ject.componote.domain.comment.domain.Comment;
import ject.componote.domain.comment.dto.like.event.CommentLikeEvent;
import ject.componote.domain.comment.dto.like.event.CommentUnlikeEvent;
import ject.componote.domain.comment.error.NotFoundCommentException;
import ject.componote.domain.common.model.Count;
import ject.componote.fixture.CommentFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static ject.componote.fixture.MemberFixture.KIM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class CommentLikeEventListenerTest {
    @Mock
    CommentRepository commentRepository;

    @Mock
    CommentLikeRepository commentLikeRepository;

    @InjectMocks
    CommentLikeEventListener commentLikeEventListener;

    final Member member = KIM.생성(1L);
    final AuthPrincipal authPrincipal = AuthPrincipal.from(member);

    @ParameterizedTest
    @DisplayName("댓글 좋아요 이벤트 처리")
    @EnumSource(CommentFixture.class)
    public void handleCommentLikeEvent(final CommentFixture fixture) throws Exception {
        // given
        final Comment comment = fixture.생성();
        final Count previousLikeCount = comment.getLikeCount();
        final Long commentId = comment.getId();
        final CommentLikeEvent event = CommentLikeEvent.of(authPrincipal, commentId);

        // when
        doReturn(Optional.of(comment)).when(commentRepository)
                .findById(commentId);
        commentLikeEventListener.handleCommentLikeEvent(event);

        // then
        final Count newLikeCount = comment.getLikeCount();
        previousLikeCount.increase();
        assertThat(previousLikeCount).isEqualTo(newLikeCount);
    }

    @ParameterizedTest
    @DisplayName("댓글 좋아요 이벤트 처리시 댓글 ID가 잘못된 경우 예외 발생")
    @EnumSource(CommentFixture.class)
    public void handleCommentLikeEventWhenInvalidCommentId(final CommentFixture fixture) throws Exception {
        // given
        final Comment comment = fixture.생성();
        final Long commentId = comment.getId();
        final CommentLikeEvent event = CommentLikeEvent.of(authPrincipal, commentId);

        // when
        doReturn(Optional.empty()).when(commentRepository)
                .findById(commentId);

        // then
        assertThatThrownBy(() -> commentLikeEventListener.handleCommentLikeEvent(event))
                .isInstanceOf(NotFoundCommentException.class);
    }

    @ParameterizedTest
    @DisplayName("댓글 좋아요 취소 이벤트 처리")
    @EnumSource(CommentFixture.class)
    public void handleCommentUnLikeEvent(final CommentFixture fixture) throws Exception {
        // given
        final Comment comment = fixture.생성();
        final Count previousLikeCount = comment.getLikeCount();
        final Long commentId = comment.getId();
        final CommentUnlikeEvent event = CommentUnlikeEvent.of(authPrincipal, commentId);

        // when
        doReturn(Optional.of(comment)).when(commentRepository)
                .findById(commentId);
        commentLikeEventListener.handleCommentUnlikeEvent(event);

        // then
        final Count newLikeCount = comment.getLikeCount();
        previousLikeCount.decrease();
        assertThat(previousLikeCount).isEqualTo(newLikeCount);
    }

    @ParameterizedTest
    @DisplayName("댓글 좋아요 취소 이벤트 처리시 댓글 ID가 잘못된 경우 예외 발생")
    @EnumSource(CommentFixture.class)
    public void handleCommentUnlikeEventWhenInvalidCommentId(final CommentFixture fixture) throws Exception {
        // given
        final Comment comment = fixture.생성();
        final Long commentId = comment.getId();
        final CommentUnlikeEvent event = CommentUnlikeEvent.of(authPrincipal, commentId);

        // when
        doReturn(Optional.empty()).when(commentRepository)
                .findById(commentId);

        // then
        assertThatThrownBy(() -> commentLikeEventListener.handleCommentUnlikeEvent(event))
                .isInstanceOf(NotFoundCommentException.class);
    }
}