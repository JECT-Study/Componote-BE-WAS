package ject.componote.domain.component.api;

import jakarta.validation.Valid;
import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.auth.model.Authenticated;
import ject.componote.domain.common.dto.response.PageResponse;
import ject.componote.domain.component.application.ComponentService;
import ject.componote.domain.component.dto.find.request.ComponentSearchRequest;
import ject.componote.domain.component.dto.find.request.ComponentSummaryRequest;
import ject.componote.domain.component.dto.find.response.ComponentSearchResponse;
import ject.componote.domain.component.dto.find.response.ComponentSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/components")
@RequiredArgsConstructor
@RestController
public class ComponentController {
    private final ComponentService componentService;

    @GetMapping
    public ResponseEntity<PageResponse<ComponentSummaryResponse>> getAllComponentSummaries(
            @Authenticated final AuthPrincipal authPrincipal,
            @ModelAttribute @Valid final ComponentSummaryRequest request,
            @PageableDefault final Pageable pageable
    ) {
        return ResponseEntity.ok(
                componentService.getAllComponentSummaries(authPrincipal, request, pageable)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<ComponentSearchResponse>> search(
            @ModelAttribute @Valid final ComponentSearchRequest request,
            @PageableDefault final Pageable pageable
    ) {
        return ResponseEntity.ok(
                componentService.search(request, pageable)
        );
    }

    @GetMapping("/{componentId}")
    public ResponseEntity<?> getComponentDetail(
            @Authenticated final AuthPrincipal authPrincipal,
            @PathVariable("componentId") final Long componentId) {
        return ResponseEntity.ok(
                componentService.getComponentDetail(authPrincipal, componentId)
        );
    }
}
