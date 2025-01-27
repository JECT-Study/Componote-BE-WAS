package ject.componote.domain.member.model;

import ject.componote.domain.auth.domain.Member;
import ject.componote.domain.auth.domain.Role;

public record AuthPrincipal(Long id, String nickname, Role role) {
    public static AuthPrincipal from(final Member Member) {
        return new AuthPrincipal(Member.getId(), Member.getNickname().getValue(), Member.getRole());
    }

    public boolean isMember() {
        return role.equals(Role.USER);
    }
}
