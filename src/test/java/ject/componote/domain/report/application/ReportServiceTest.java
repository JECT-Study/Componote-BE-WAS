package ject.componote.domain.report.application;

import ject.componote.domain.member.model.AuthPrincipal;
import ject.componote.domain.comment.dao.CommentRepository;
import ject.componote.domain.comment.domain.Comment;
import ject.componote.domain.comment.dto.report.event.CommentReportEvent;
import ject.componote.domain.comment.error.NotFoundCommentException;
import ject.componote.domain.report.dao.ReportRepository;
import ject.componote.domain.report.domain.ReportReason;
import ject.componote.domain.report.dto.request.ReportRequest;
import ject.componote.domain.report.error.AlreadyReportedException;
import ject.componote.domain.report.error.SelfReportNotAllowedException;
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
class ReportServiceTest {
    @Mock
    ApplicationEventPublisher eventPublisher;

    @Mock
    CommentRepository commentRepository;

    @Mock
    ReportRepository reportRepository;

    @InjectMocks
    ReportService reportService;

    AuthPrincipal authPrincipal = AuthPrincipal.from(KIM.생성(1L));

    @ParameterizedTest
    @DisplayName("댓글 신고")
    @EnumSource(value = CommentFixture.class)
    public void reportComment(final CommentFixture fixture) throws Exception {
        // given
        final Comment comment = fixture.생성();
        final Long commentId = comment.getId();
        final Long memberId = authPrincipal.id();
        final ReportRequest request = new ReportRequest(ReportReason.OBSCENE_CONTENT);
        final CommentReportEvent event = CommentReportEvent.from(commentId);

        // when
        doReturn(true).when(commentRepository)
                .existsById(commentId);
        doReturn(false).when(commentRepository)
                .existsByIdAndMemberId(commentId, memberId);
        doReturn(false).when(reportRepository)
                .existsByCommentIdAndMemberId(commentId, memberId);
        doNothing().when(eventPublisher)
                .publishEvent(event);

        // then
        assertDoesNotThrow(() -> reportService.reportComment(authPrincipal, commentId, request));
    }

    @ParameterizedTest
    @DisplayName("댓글 ID가 잘못된 경우 예외 발생")
    @EnumSource(value = CommentFixture.class)
    public void reportCommentWhenInvalidCommentId(final CommentFixture fixture) throws Exception {
        // given
        final Comment comment = fixture.생성();
        final Long commentId = comment.getId();
        final ReportRequest request = new ReportRequest(ReportReason.OBSCENE_CONTENT);

        // when
        doReturn(false).when(commentRepository)
                .existsById(commentId);
        // then
        assertThatThrownBy(() -> reportService.reportComment(authPrincipal, commentId, request))
                .isInstanceOf(NotFoundCommentException.class);
    }

    @ParameterizedTest
    @DisplayName("본인 댓글에 신고시 예외 발생")
    @EnumSource(value = CommentFixture.class)
    public void reportCommentWhenSelfReport(final CommentFixture fixture) throws Exception {
        // given
        final Comment comment = fixture.생성();
        final Long commentId = comment.getId();
        final Long memberId = authPrincipal.id();
        final ReportRequest request = new ReportRequest(ReportReason.OBSCENE_CONTENT);

        // when
        doReturn(true).when(commentRepository)
                .existsById(commentId);
        doReturn(true).when(commentRepository)
                .existsByIdAndMemberId(commentId, memberId);

        // then
        assertThatThrownBy(() -> reportService.reportComment(authPrincipal, commentId, request))
                .isInstanceOf(SelfReportNotAllowedException.class);
    }

    @ParameterizedTest
    @DisplayName("중복 신고시 예외 발생")
    @EnumSource(value = CommentFixture.class)
    public void reportCommentWhenAlreadyReported(final CommentFixture fixture) throws Exception {
        // given
        final Comment comment = fixture.생성();
        final Long commentId = comment.getId();
        final Long memberId = authPrincipal.id();
        final ReportRequest request = new ReportRequest(ReportReason.OBSCENE_CONTENT);

        // when
        doReturn(true).when(commentRepository)
                .existsById(commentId);
        doReturn(false).when(commentRepository)
                .existsByIdAndMemberId(commentId, memberId);
        doReturn(true).when(reportRepository)
                .existsByCommentIdAndMemberId(commentId, memberId);

        // then
        assertThatThrownBy(() -> reportService.reportComment(authPrincipal, commentId, request))
                .isInstanceOf(AlreadyReportedException.class);
    }
}