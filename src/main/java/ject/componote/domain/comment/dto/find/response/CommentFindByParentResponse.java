package ject.componote.domain.comment.dto.find.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import ject.componote.domain.comment.dao.CommentFindByParentDao;

import java.time.LocalDateTime;

public record CommentFindByParentResponse(
        CommentProfileResponse profile,
        Long id,
        @JsonInclude(JsonInclude.Include.NON_NULL) String imageUrl,
        String content,
        LocalDateTime createdAt,
        Long likeCount,
        boolean isLiked
) {
    public static CommentFindByParentResponse from(final CommentFindByParentDao dto) {
        return new CommentFindByParentResponse(
                createProfileResponse(dto),
                dto.commentId(),
                dto.commentImage().getImage().toUrl(),
                dto.content().getValue(),
                dto.createdAt(),
                dto.likeCount().getValue(),
                dto.isLiked()
        );
    }

    private static CommentProfileResponse createProfileResponse(final CommentFindByParentDao dto) {
        return new CommentProfileResponse(
                dto.memberId(),
                dto.nickname().getValue(),
                dto.profileImage().getImage().toUrl(),
                dto.job().name()
        );
    }
}
