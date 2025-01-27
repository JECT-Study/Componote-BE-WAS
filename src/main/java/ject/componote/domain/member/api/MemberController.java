package ject.componote.domain.member.api;

import jakarta.validation.Valid;
import ject.componote.domain.member.application.MemberService;
import ject.componote.domain.member.dto.find.response.MemberSummaryResponse;
import ject.componote.domain.member.dto.update.request.MemberNicknameUpdateRequest;
import ject.componote.domain.member.dto.update.request.MemberProfileImageUpdateRequest;
import ject.componote.domain.member.dto.update.request.MemberEmailUpdateRequest;
import ject.componote.domain.auth.dto.verify.request.MemberEmailVerificationRequest;
import ject.componote.domain.member.model.AuthPrincipal;
import ject.componote.domain.member.model.Authenticated;
import ject.componote.domain.member.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/members")
@RequiredArgsConstructor
@RestController
@User
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/summary")
    public ResponseEntity<MemberSummaryResponse> getMemberSummary(@Authenticated final AuthPrincipal authPrincipal) {
        return ResponseEntity.ok(
                memberService.getMemberSummary(authPrincipal)
        );
    }

    @PutMapping("/profile-image")
    public ResponseEntity<Void> updateProfileImage(@Authenticated final AuthPrincipal authPrincipal,
                                                   @RequestBody @Valid final MemberProfileImageUpdateRequest request) {
        memberService.updateProfileImage(authPrincipal, request);
        return ResponseEntity.noContent()
                .build();
    }

    @PutMapping("/nickname")
    public ResponseEntity<Void> updateNickname(@Authenticated final AuthPrincipal authPrincipal,
                                               @RequestBody @Valid final MemberNicknameUpdateRequest request) {
        memberService.updateNickname(authPrincipal, request);
        return ResponseEntity.noContent()
                .build();
    }

    @PutMapping("/email")
    public ResponseEntity<Void> updateEmail(@Authenticated final AuthPrincipal authPrincipal,
                                            @RequestBody @Valid final MemberEmailUpdateRequest request) {
        memberService.updateEmail(authPrincipal, request);
        return ResponseEntity.noContent()
                .build();
    }

    @PostMapping("/email/verification")
    public ResponseEntity<Void> sendVerificationCode(@Authenticated final AuthPrincipal authPrincipal,
                                                     @RequestBody @Valid final MemberEmailVerificationRequest request) {
        memberService.sendVerificationCode(authPrincipal, request);
        return ResponseEntity.noContent()
                .build();
    }
}
