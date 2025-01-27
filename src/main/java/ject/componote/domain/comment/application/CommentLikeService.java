package ject.componote.domain.comment.application;

import ject.componote.domain.member.model.AuthPrincipal;
import ject.componote.domain.comment.dao.CommentLikeRepository;
import ject.componote.domain.comment.dto.like.event.CommentLikeEvent;
import ject.componote.domain.comment.dto.like.event.CommentUnlikeEvent;
import ject.componote.domain.comment.error.AlreadyLikedException;
import ject.componote.domain.comment.error.NoLikedException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentLikeService {
    private final ApplicationEventPublisher eventPublisher;
    private final CommentLikeRepository commentLikeRepository;

    public void likeComment(final AuthPrincipal authPrincipal, final Long commentId) {
        final Long memberId = authPrincipal.id();
        if (isAlreadyLiked(commentId, memberId)) {
            throw new AlreadyLikedException(commentId, memberId);
        }
        eventPublisher.publishEvent(CommentLikeEvent.of(authPrincipal, commentId));
    }

    public void unlikeComment(final AuthPrincipal authPrincipal, final Long commentId) {
        final Long memberId = authPrincipal.id();
        if (!isAlreadyLiked(commentId, memberId)) {
            throw new NoLikedException(commentId, memberId);
        }
        eventPublisher.publishEvent(CommentUnlikeEvent.of(authPrincipal, commentId));
    }

    private boolean isAlreadyLiked(final Long commentId, final Long memberId) {
        return commentLikeRepository.existsByCommentIdAndMemberId(commentId, memberId);
    }
}
