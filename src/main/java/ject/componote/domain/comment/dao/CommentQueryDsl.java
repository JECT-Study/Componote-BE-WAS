package ject.componote.domain.comment.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentQueryDsl {
    Page<CommentFindByComponentDao> findAllByComponentIdWithPagination(final Long componentId, final Pageable pageable);
    Page<CommentFindByComponentDao> findAllByComponentIdWithLikeStatusAndPagination(final Long componentId, final Long memberId, final Pageable pageable);
    Page<CommentFindByParentDao> findAllByParentIdWithPagination(final Long parentId, final Pageable pageable);
    Page<CommentFindByParentDao> findAllByParentIdWithLikeStatusAndPagination(final Long parentId, final Long memberId, final Pageable pageable);
    Page<CommentFindByMemberDao> findAllByMemberIdWithPagination(final Long memberId, final Pageable pageable);
    boolean isRootComment(final Long id);
}
