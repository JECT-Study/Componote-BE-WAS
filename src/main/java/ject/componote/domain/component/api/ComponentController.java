package ject.componote.domain.component.api;

import jakarta.validation.Valid;
import ject.componote.domain.member.model.AuthPrincipal;
import ject.componote.domain.member.model.Authenticated;
import ject.componote.domain.common.dto.response.PageResponse;
import ject.componote.domain.component.application.ComponentService;
import ject.componote.domain.component.dto.find.request.ComponentSearchRequest;
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

    @GetMapping("/search")
    public ResponseEntity<PageResponse<ComponentSummaryResponse>> search(
            @Authenticated final AuthPrincipal authPrincipal,
            @ModelAttribute @Valid final ComponentSearchRequest request,
            @PageableDefault final Pageable pageable
            ) {
        return ResponseEntity.ok(
                componentService.search(authPrincipal, request, pageable)
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
