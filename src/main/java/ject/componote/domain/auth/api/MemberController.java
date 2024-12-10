package ject.componote.domain.auth.api;

import ject.componote.domain.auth.application.MemberService;
import ject.componote.domain.auth.dto.find.response.MemberSummaryResponse;
import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.auth.model.Authenticated;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/members")
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/summary")
    public ResponseEntity<MemberSummaryResponse> getSummary(@Authenticated final AuthPrincipal authPrincipal) {
        return ResponseEntity.ok()
                .body(memberService.getSummary(authPrincipal));
    }
}
