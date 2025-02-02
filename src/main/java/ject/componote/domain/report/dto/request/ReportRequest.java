package ject.componote.domain.report.dto.request;

import jakarta.validation.constraints.NotNull;
import ject.componote.domain.report.domain.ReportReason;

public record ReportRequest(@NotNull ReportReason reason) {
}
