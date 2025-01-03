package ject.componote.domain.comment.dto.find.response;

import ject.componote.domain.comment.dao.CommentProfileDao;

public record CommentProfileResponse(Long memberId, String nickname, String profileImageUrl, String job) {
    public static CommentProfileResponse from(final CommentProfileDao profileDto) {
        return new CommentProfileResponse(
                profileDto.memberId(),
                profileDto.nickname().getValue(),
                profileDto.profileImage().getObjectKey(),
                profileDto.job().name()
        );
    }
}
