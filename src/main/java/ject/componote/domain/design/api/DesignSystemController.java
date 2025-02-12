package ject.componote.domain.design.api;

import jakarta.validation.Valid;
import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.auth.model.Authenticated;
import ject.componote.domain.common.dto.response.PageResponse;
import ject.componote.domain.design.application.DesignSystemService;
import ject.componote.domain.design.dto.search.request.DesignSystemSearchRequest;
import ject.componote.domain.design.dto.search.request.DesignSystemSummaryRequest;
import ject.componote.domain.design.dto.search.response.DesignSystemSearchResponse;
import ject.componote.domain.design.dto.search.response.DesignSystemSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/design-systems")
@RequiredArgsConstructor
@RestController
public class DesignSystemController {
    private final DesignSystemService designSystemService;

    @GetMapping
    public ResponseEntity<PageResponse<DesignSystemSummaryResponse>> getAllDesignSummaries(
            @Authenticated final AuthPrincipal authPrincipal,
            @ModelAttribute @Valid final DesignSystemSummaryRequest request,
            @PageableDefault final Pageable pageable
    ) {
        return ResponseEntity.ok(
                designSystemService.getAllDesignSummaries(authPrincipal, request, pageable)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<DesignSystemSearchResponse>> searchDesigns(
            @ModelAttribute @Valid final DesignSystemSearchRequest request,
            @PageableDefault final Pageable pageable
    ) {
        return ResponseEntity.ok(
                designSystemService.searchDesigns(request, pageable)
        );
    }
}
