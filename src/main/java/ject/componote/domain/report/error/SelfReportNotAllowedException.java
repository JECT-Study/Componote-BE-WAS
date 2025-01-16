package ject.componote.domain.report.error;

import org.springframework.http.HttpStatus;

public class SelfReportNotAllowedException extends ReportException {
    public SelfReportNotAllowedException() {
        super("본인이 작성한 댓글은 신고할 수 없습니다.", HttpStatus.BAD_REQUEST);
    }
}
