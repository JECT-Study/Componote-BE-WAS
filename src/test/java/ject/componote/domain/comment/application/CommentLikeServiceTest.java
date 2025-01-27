package ject.componote.domain.comment.application;

import ject.componote.domain.auth.domain.Member;
import ject.componote.domain.member.model.AuthPrincipal;
import ject.componote.domain.comment.dao.CommentLikeRepository;
import ject.componote.domain.comment.domain.Comment;
import ject.componote.domain.comment.dto.like.event.CommentLikeEvent;
import ject.componote.domain.comment.dto.like.event.CommentUnlikeEvent;
import ject.componote.domain.comment.error.AlreadyLikedException;
import ject.componote.domain.comment.error.NoLikedException;
import ject.componote.fixture.CommentFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static ject.componote.fixture.MemberFixture.KIM;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class CommentLikeServiceTest {
    @Mock
    ApplicationEventPublisher eventPublisher;

    @Mock
    CommentLikeRepository commentLikeRepository;

    @InjectMocks
    CommentLikeService commentLikeService;

    final Member member = KIM.생성(1L);
    final AuthPrincipal authPrincipal = AuthPrincipal.from(member);

    @ParameterizedTest
    @DisplayName("댓글 좋아요")
    @EnumSource(value = CommentFixture.class)
    public void likeComment(final CommentFixture fixture) throws Exception {
        // given
        final Comment comment = fixture.생성();
        final Long commentId = comment.getId();
        final Long memberId = authPrincipal.id();
        final CommentLikeEvent event = CommentLikeEvent.of(authPrincipal, commentId);

        // when
        doReturn(false).when(commentLikeRepository)
                .existsByCommentIdAndMemberId(commentId, memberId);
        doNothing().when(eventPublisher)
                .publishEvent(event);

        // then
        assertDoesNotThrow(
                () -> commentLikeService.likeComment(authPrincipal, commentId)
        );
    }

    @ParameterizedTest
    @DisplayName("댓글 좋아요 취소")
    @EnumSource(value = CommentFixture.class)
    public void unlikeComment(final CommentFixture fixture) throws Exception {
        // given
        final Comment comment = fixture.생성();
        final Long commentId = comment.getId();
        final Long memberId = authPrincipal.id();
        final CommentUnlikeEvent event = CommentUnlikeEvent.of(authPrincipal, commentId);

        // when
        doReturn(true).when(commentLikeRepository)
                .existsByCommentIdAndMemberId(commentId, memberId);
        doNothing().when(eventPublisher)
                .publishEvent(event);

        // then
        assertDoesNotThrow(
                () -> commentLikeService.unlikeComment(authPrincipal, commentId)
        );
    }

    @ParameterizedTest
    @DisplayName("댓글 좋아요시 좋아요를 이미 좋아요를 눌렀다면 예외 발생")
    @EnumSource(value = CommentFixture.class)
    public void likeCommentWhenAlreadyLiked(final CommentFixture fixture) throws Exception {
        // given
        final Comment comment = fixture.생성();
        final Long commentId = comment.getId();
        final Long memberId = authPrincipal.id();

        // when
        doReturn(true).when(commentLikeRepository)
                .existsByCommentIdAndMemberId(commentId, memberId);

        // then
        assertThatThrownBy(() -> commentLikeService.likeComment(authPrincipal, commentId))
                .isInstanceOf(AlreadyLikedException.class);
    }

    @ParameterizedTest
    @DisplayName("댓글 좋아요 취소시 좋아요를 누른적이 없는 경우 예외 발생")
    @EnumSource(value = CommentFixture.class)
    public void unlikeCommentWhenNoLike(final CommentFixture fixture) throws Exception {
        // given
        final Comment comment = fixture.생성();
        final Long commentId = comment.getId();
        final Long memberId = authPrincipal.id();

        // when
        doReturn(false).when(commentLikeRepository)
                .existsByCommentIdAndMemberId(commentId, memberId);

        // then
        assertThatThrownBy(() -> commentLikeService.unlikeComment(authPrincipal, commentId))
                .isInstanceOf(NoLikedException.class);
    }
}