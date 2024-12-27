package ject.componote.domain.comment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import ject.componote.domain.comment.model.CommentContent;
import ject.componote.domain.comment.model.converter.CommentContentConverter;
import ject.componote.domain.common.domain.BaseEntity;
import ject.componote.domain.common.model.Count;
import ject.componote.domain.common.model.Image;
import ject.componote.domain.common.model.converter.CountConverter;
import ject.componote.domain.common.model.converter.ImageConverter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    @Convert(converter = CommentContentConverter.class)
    private CommentContent content;

    @Column(name = "image", nullable = true)
    @Convert(converter = ImageConverter.class)
    private Image image;

    @Column(name = "report_count", nullable = false)
    @Convert(converter = CountConverter.class)
    private Count reportCount;

    @Column(name = "like_count", nullable = false)
    @Convert(converter = CountConverter.class)
    private Count likeCount;

    @Column(name = "component_id", nullable = false)
    private Long componentId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "parent_id", nullable = true)
    private Long parentId;

    private Comment(final Long componentId, final Long memberId, final Long parentId, final String content, final Image image) {
        this.componentId = componentId;
        this.memberId = memberId;
        this.parentId = parentId;
        this.content = CommentContent.from(content);
        this.image = image;
        this.likeCount = Count.create();
        this.reportCount = Count.create();
    }

    public static Comment createWithImage(final Long componentId, final Long memberId, final String content, final Image image) {
        return new Comment(componentId, memberId, null, content, image);
    }

    public static Comment createWithoutImage(final Long componentId, final Long memberId, final String  content) {
        return new Comment(componentId, memberId, null, content, null);
    }

    public static Comment createReplyWithoutImage(final Long componentId, final Long memberId, final Comment parentComment, final String  content) {
        return new Comment(componentId, memberId, parentComment.getParentId(), content, null);
    }

    public static Comment createReplyWithImage(final Long componentId, final Long memberId, final Comment parentComment, final String  content, final Image image) {
        return new Comment(componentId, memberId, parentComment.getParentId(), content, image);
    }
}
