package ject.componote.fixture;

import ject.componote.domain.comment.application.CommentCreationStrategy;
import ject.componote.domain.comment.domain.Comment;
import ject.componote.domain.comment.dto.create.request.CommentCreateRequest;

public enum CommentFixture {
    댓글_이미지X(1L, 1L, null, "일반 댓글입니다.", null),
    댓글_이미지O(1L, 1L, null, "일반 댓글입니다.", "comments/image1.jpg"),
    답글_이미지X(1L, 1L, 100L, "답글입니다.", null),
    답글_이미지O(1L, 1L, 100L, "답글입니다.", "comments/image2.jpg");

    private final Long componentId;
    private final Long memberId;
    private final Long parentId;
    private final String content;
    private final String imageObjectKey;

    CommentFixture(Long componentId, Long memberId, Long parentId, String content, String imageObjectKey) {
        this.componentId = componentId;
        this.memberId = memberId;
        this.parentId = parentId;
        this.content = content;
        this.imageObjectKey = imageObjectKey;
    }

    public Comment 생성(final Long memberId) {
        return CommentCreationStrategy.createBy(toCreateRequest(), memberId);
    }

    public Comment 생성() {
        return CommentCreationStrategy.createBy(toCreateRequest(), memberId);
    }

    public CommentCreateRequest toCreateRequest() {
        return new CommentCreateRequest(imageObjectKey, content, componentId, parentId);
    }
}
