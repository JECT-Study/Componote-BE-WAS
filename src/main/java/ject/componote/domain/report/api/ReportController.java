package ject.componote.domain.report.api;

import jakarta.validation.Valid;
import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.auth.model.Authenticated;
import ject.componote.domain.auth.model.User;
import ject.componote.domain.report.application.ReportService;
import ject.componote.domain.report.dto.request.ReportRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/reports")
@RestController
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping("/{commentId}")
    @User
    public ResponseEntity<Void> create(@Authenticated final AuthPrincipal authPrincipal,
                                       @PathVariable("commentId") final Long commentId,
                                       @RequestBody @Valid final ReportRequest request) {
        reportService.create(authPrincipal, commentId, request);
        return ResponseEntity.noContent()
                .build();
    }
}
