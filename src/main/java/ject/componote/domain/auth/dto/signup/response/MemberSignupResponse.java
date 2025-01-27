package ject.componote.domain.auth.dto.signup.response;

import ject.componote.domain.member.domain.Member;

public record MemberSignupResponse(String accessToken, Long memberId) {
    public static MemberSignupResponse of(final String accessToken, final Member member) {
        return new MemberSignupResponse(accessToken, member.getId());
    }
}
