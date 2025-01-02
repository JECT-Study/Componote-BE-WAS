package ject.componote.domain.report.application;

import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.comment.dao.CommentRepository;
import ject.componote.domain.comment.dto.report.event.CommentReportEvent;
import ject.componote.domain.comment.error.NotFoundCommentException;
import ject.componote.domain.report.dao.ReportRepository;
import ject.componote.domain.report.domain.Report;
import ject.componote.domain.report.domain.ReportReason;
import ject.componote.domain.report.dto.request.ReportRequest;
import ject.componote.domain.report.error.AlreadyReportedException;
import ject.componote.domain.report.error.SelfReportNotAllowedException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {
    private final ApplicationEventPublisher eventPublisher;
    private final CommentRepository commentRepository;
    private final ReportRepository reportRepository;

    @Transactional
    public void reportComment(final AuthPrincipal authPrincipal, final Long commentId, final ReportRequest request) {
        final Long memberId = authPrincipal.id();
        final ReportReason reason = request.reason();

        validateCommentId(commentId);
        validateOwnerCommentReport(commentId, memberId);
        validateAlreadyReported(commentId, memberId);

        reportRepository.save(Report.of(reason, commentId, memberId));
        eventPublisher.publishEvent(CommentReportEvent.from(commentId));
    }

    private void validateCommentId(final Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new NotFoundCommentException(commentId);
        }
    }

    private void validateOwnerCommentReport(final Long commentId, final Long memberId) {
        if (commentRepository.existsByIdAndMemberId(commentId, memberId)) {
            throw new SelfReportNotAllowedException();
        }
    }

    private void validateAlreadyReported(final Long commentId, final Long memberId) {
        if (reportRepository.existsByCommentIdAndMemberId(commentId, memberId)) {
            throw new AlreadyReportedException();
        }
    }
}
