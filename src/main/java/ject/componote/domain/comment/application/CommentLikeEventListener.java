package ject.componote.domain.comment.application;

import ject.componote.domain.comment.dao.CommentLikeRepository;
import ject.componote.domain.comment.dao.CommentRepository;
import ject.componote.domain.comment.domain.Comment;
import ject.componote.domain.comment.domain.CommentLike;
import ject.componote.domain.comment.dto.like.event.CommentLikeEvent;
import ject.componote.domain.comment.dto.like.event.CommentLikeNotificationEvent;
import ject.componote.domain.comment.dto.like.event.CommentUnlikeEvent;
import ject.componote.domain.comment.error.NotFoundCommentException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentLikeEventListener {
    private final ApplicationEventPublisher eventPublisher;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Async
    @EventListener
    @Transactional
    public void handleCommentLikeEvent(final CommentLikeEvent event) {
        final Long commentId = event.commentId();
        final Comment comment = findCommentById(commentId);
        comment.increaseLikeCount();

        final Long memberId = event.memberId();
        commentLikeRepository.save(CommentLike.of(commentId, memberId));

        if (!isSelfLiked(comment, memberId)) {
            eventPublisher.publishEvent(CommentLikeNotificationEvent.of(comment, memberId));;
        }
    }

    @Async
    @EventListener
    @Transactional
    public void handleCommentUnlikeEvent(final CommentUnlikeEvent event) {
        final Long commentId = event.commentId();
        final Comment comment = findCommentById(commentId);
        comment.decreaseLikeCount();

        final Long memberId = event.memberId();
        commentLikeRepository.deleteByCommentIdAndMemberId(commentId, memberId);
        commentRepository.save(comment);
    }

    private Comment findCommentById(final Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundCommentException(commentId));
    }

    private boolean isSelfLiked(final Comment comment, final Long memberId) {
        return Objects.equals(comment.getMemberId(), memberId);
    }
}
