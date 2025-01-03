package ject.componote.domain.comment.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentQueryDsl {
    Page<CommentFindByComponentDao> findAllByComponentIdWithPagination(final Long componentId, final Pageable pageable);
    Page<CommentFindByComponentDao> findAllByComponentIdWithLikeStatusAndPagination(final Long componentId, final Long memberId, final Pageable pageable);
    Page<CommentFindByMemberDao> findAllByMemberIdWithPagination(final Long memberId, final Pageable pageable);
}
