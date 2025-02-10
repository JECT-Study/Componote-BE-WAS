package ject.componote.domain.auth.dto.image.event;

import ject.componote.domain.auth.domain.Member;
import ject.componote.domain.auth.model.ProfileImage;

public record ProfileImageMoveEvent(ProfileImage image) {
    public static ProfileImageMoveEvent from(final Member member) {
        return new ProfileImageMoveEvent(member.getProfileImage());
    }
}
