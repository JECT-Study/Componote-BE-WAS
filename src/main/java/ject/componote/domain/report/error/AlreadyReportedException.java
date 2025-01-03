package ject.componote.domain.report.error;

import org.springframework.http.HttpStatus;

public class AlreadyReportedException extends ReportException {
    public AlreadyReportedException() {
        super("이미 신고한 댓글입니다.", HttpStatus.BAD_REQUEST);
    }
}
