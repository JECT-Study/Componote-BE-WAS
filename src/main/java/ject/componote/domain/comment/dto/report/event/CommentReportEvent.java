package ject.componote.domain.comment.dto.report.event;

public record CommentReportEvent(Long commentId) {
    public static CommentReportEvent from(final Long commentId) {
        return new CommentReportEvent(commentId);
    }
}
