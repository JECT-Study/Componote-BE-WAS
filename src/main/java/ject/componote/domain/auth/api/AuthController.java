package ject.componote.domain.auth.api;

import jakarta.validation.Valid;
import ject.componote.domain.auth.application.AuthService;
import ject.componote.domain.auth.dto.authorize.request.MemberAuthorizeRequest;
import ject.componote.domain.auth.dto.authorize.response.MemberAuthorizeResponse;
import ject.componote.domain.auth.dto.login.request.MemberLoginRequest;
import ject.componote.domain.auth.dto.login.response.MemberLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/oauth")
@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;

    @GetMapping("/authorize")
    public ResponseEntity<MemberAuthorizeResponse> authorize(@ModelAttribute @Valid final MemberAuthorizeRequest memberAuthorizeRequest) {
        return ResponseEntity.ok()
                .body(authService.authorize(memberAuthorizeRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponse> login(@RequestBody @Valid final MemberLoginRequest memberLoginRequest) {
        return ResponseEntity.ok()
                .body(authService.login(memberLoginRequest));
    }
}
