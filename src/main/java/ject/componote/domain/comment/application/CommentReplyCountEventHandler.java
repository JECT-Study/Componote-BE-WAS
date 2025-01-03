package ject.componote.domain.comment.application;

import ject.componote.domain.comment.dao.CommentRepository;
import ject.componote.domain.comment.domain.Comment;
import ject.componote.domain.comment.dto.reply.event.CommentReplyCountIncreaseEvent;
import ject.componote.domain.comment.error.NotFoundCommentException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentReplyCountEventHandler {
    private final CommentRepository commentRepository;

    @Async
    @EventListener
    @Transactional
    public void handleCommentReplyCountIncreaseEvent(final CommentReplyCountIncreaseEvent event) {
        final Comment comment = findCommentById(event.parentId());
        comment.increaseReplyCount();
    }

    public Comment findCommentById(final Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundCommentException(commentId));
    }
}
