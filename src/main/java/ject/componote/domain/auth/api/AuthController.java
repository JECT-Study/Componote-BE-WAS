package ject.componote.domain.auth.api;

import jakarta.validation.Valid;
import ject.componote.domain.auth.application.AuthService;
import ject.componote.domain.auth.dto.login.request.MemberLoginRequest;
import ject.componote.domain.auth.dto.login.response.MemberLoginResponse;
import ject.componote.domain.auth.dto.signup.request.MemberSignupRequest;
import ject.componote.domain.auth.dto.signup.response.MemberSignupResponse;
import ject.componote.domain.auth.dto.validate.request.MemberNicknameValidateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<MemberSignupResponse> signup(@RequestBody @Valid final MemberSignupRequest memberSignupRequest) {
        return ResponseEntity.ok(
                authService.signup(memberSignupRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponse> login(@RequestBody @Valid final MemberLoginRequest memberLoginRequest) {
        return ResponseEntity.ok(
                authService.login(memberLoginRequest)
        );
    }

    @PostMapping("/nickname/validation")
    public ResponseEntity<Void> validateNickname(@RequestBody @Valid final MemberNicknameValidateRequest request) {
        authService.validateNickname(request);
        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> delete(@PathVariable final Long memberId) {
        authService.delete(memberId);
        return ResponseEntity.noContent()
                .build();
    }
}
