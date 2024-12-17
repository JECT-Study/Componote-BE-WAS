package ject.componote.domain.auth.dto.find.response;

import ject.componote.domain.auth.dto.find.MemberSummaryDto;

public record MemberSummaryResponse(String nickname, String profileImageUrl) {
    public static MemberSummaryResponse from(MemberSummaryDto memberSummaryDto) {
        return new MemberSummaryResponse(
                memberSummaryDto.nickname().getValue(),
                memberSummaryDto.profileImage().getObjectKey()
        );
    }
}
