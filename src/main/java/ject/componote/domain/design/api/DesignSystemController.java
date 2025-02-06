package ject.componote.domain.design.api;

import jakarta.validation.Valid;
import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.auth.model.Authenticated;
import ject.componote.domain.common.dto.response.PageResponse;
import ject.componote.domain.design.application.DesignSystemService;
import ject.componote.domain.design.dto.search.request.DesignSystemSearchRequest;
import ject.componote.domain.design.dto.search.response.DesignSystemSearchResponse;
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

    @GetMapping("/search")
    public ResponseEntity<PageResponse<DesignSystemSearchResponse>> searchDesignSystem(
            @Authenticated final AuthPrincipal authPrincipal,
            @ModelAttribute @Valid final DesignSystemSearchRequest designSystemSearchRequest,
            @PageableDefault final Pageable pageable
    ) {
        return ResponseEntity.ok(
                designSystemService.searchDesignSystem(authPrincipal, designSystemSearchRequest, pageable)
        );
    }
}
