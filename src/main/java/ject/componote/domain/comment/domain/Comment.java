package ject.componote.domain.comment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import ject.componote.domain.comment.model.CommentContent;
import ject.componote.domain.comment.model.CommentImage;
import ject.componote.domain.comment.model.converter.CommentContentConverter;
import ject.componote.domain.comment.model.converter.CommentImageConverter;
import ject.componote.domain.common.domain.BaseEntity;
import ject.componote.domain.common.model.Count;
import ject.componote.domain.common.model.converter.CountConverter;
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
    @Convert(converter = CommentImageConverter.class)
    private CommentImage image;

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

    private Comment(final Long componentId, final Long memberId, final Long parentId, final String content, final String objectKey) {
        this.componentId = componentId;
        this.memberId = memberId;
        this.parentId = parentId;
        this.content = CommentContent.from(content);
        this.image = CommentImage.from(objectKey);
        this.likeCount = Count.create();
        this.reportCount = Count.create();
    }

    public static Comment createWithImage(final Long componentId, final Long memberId, final String content, final String objectKey) {
        return new Comment(componentId, memberId, null, content, objectKey);
    }

    public static Comment createWithoutImage(final Long componentId, final Long memberId, final String content) {
        return new Comment(componentId, memberId, null, content, null);
    }

    public static Comment createReplyWithoutImage(final Long componentId, final Long memberId, final Long parentId, final String content) {
        return new Comment(componentId, memberId, parentId, content, null);
    }

    public static Comment createReplyWithImage(final Long componentId, final Long memberId, final Long parentId, final String content, final String objectKey) {
        return new Comment(componentId, memberId, parentId, content, objectKey);
    }

    public void increaseLikeCount() {
        this.likeCount.increase();
    }

    public void decreaseLikeCount() {
        this.likeCount.decrease();
    }

    public boolean equalsImage(final CommentImage image) {
        return this.image.equals(image);
    }

    public void update(final CommentContent content, final CommentImage image) {
        updateContent(content);
        updateImage(image);
    }

    private void updateContent(final CommentContent content) {
        this.content = content;
    }

    private void updateImage(final CommentImage image) {
        this.image = image;
    }
}
