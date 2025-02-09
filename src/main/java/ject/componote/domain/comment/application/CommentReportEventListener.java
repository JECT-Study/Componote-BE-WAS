package ject.componote.domain.comment.application;

import ject.componote.domain.comment.dao.CommentRepository;
import ject.componote.domain.comment.domain.Comment;
import ject.componote.domain.comment.error.NotFoundCommentException;
import ject.componote.domain.comment.dto.report.event.CommentReportEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentReportEventListener {
    private final CommentRepository commentRepository;

    @Async
    @EventListener
    @Transactional
    public void handleCommentReportEvent(final CommentReportEvent event) {
        final Comment comment = findCommentById(event.commentId());
        comment.increaseReportCount();
    }

    private Comment findCommentById(final Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundCommentException(commentId));
    }
}
