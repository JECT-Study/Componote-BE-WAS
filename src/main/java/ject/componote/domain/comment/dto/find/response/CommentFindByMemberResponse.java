package ject.componote.domain.comment.dto.find.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import ject.componote.domain.comment.dao.CommentFindByMemberDao;

import java.time.LocalDateTime;

public record CommentFindByMemberResponse(Long id, @JsonInclude(JsonInclude.Include.NON_NULL) Long parentId,
                                          String componentTitle,
                                          @JsonInclude(JsonInclude.Include.NON_NULL) String parentContent,
                                          String content, LocalDateTime createdAt, boolean isReply) {
    public static CommentFindByMemberResponse from(final CommentFindByMemberDao commentFindByMemberDao) {
        return new CommentFindByMemberResponse(
                commentFindByMemberDao.id(),
                commentFindByMemberDao.isReply() ? commentFindByMemberDao.parentId() : null,
                commentFindByMemberDao.componentTitle(),
                commentFindByMemberDao.isReply() ? commentFindByMemberDao.parentContent().getValue() : null,
                commentFindByMemberDao.content().getValue(),
                commentFindByMemberDao.createdAt(),
                commentFindByMemberDao.isReply()
        );
    }
}
