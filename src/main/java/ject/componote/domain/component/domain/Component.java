package ject.componote.domain.component.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import ject.componote.domain.common.domain.BaseEntity;
import ject.componote.domain.common.model.Count;
import ject.componote.domain.common.model.converter.CountConverter;
import ject.componote.domain.component.domain.block.ContentBlock;
import ject.componote.domain.component.domain.summary.ComponentSummary;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@DynamicUpdate
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Component extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ComponentType type;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "component_id")
    private List<MixedName> mixedNames = new ArrayList<>();

    @Embedded
    private ComponentSummary summary;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "component_id")
    private List<ContentBlock> contentBlocks = new ArrayList<>();

    @Column(name = "bookmark_count", nullable = false)
    @Convert(converter = CountConverter.class)
    private Count bookmarkCount;

    @Column(name = "comment_count", nullable = false)
    @Convert(converter = CountConverter.class)
    private Count commentCount;

    @Column(name = "design_reference_count", nullable = false)
    @Convert(converter = CountConverter.class)
    private Count designReferenceCount;

    @Column(name = "view_count", nullable = false)
    @Convert(converter = CountConverter.class)
    private Count viewCount;

    private Component(final ComponentType type, final List<String> mixedNames, final ComponentSummary summary, final List<ContentBlock> contentBlocks) {
        this.type = type;
        this.mixedNames.addAll(parseMixedNames(mixedNames));
        this.summary = summary;
        this.contentBlocks.addAll(contentBlocks);
        this.bookmarkCount = Count.create();
        this.commentCount = Count.create();
        this.designReferenceCount = Count.create();
        this.viewCount = Count.create();
    }

    public static Component of(final String title, final String introduction, final String thumbnailObjectKey, final ComponentType type, final List<String> mixedNames, final List<ContentBlock> contentBlocks) {
        return new Component(type, mixedNames, ComponentSummary.of(title, introduction, thumbnailObjectKey), contentBlocks);
    }

    public void increaseViewCount() {
        this.viewCount.increase();
    }

    public void increaseCommentCount() {
        this.commentCount.increase();
    }

    public void decreaseCommentCount() {
        this.commentCount.decrease();
    }

    private List<MixedName> parseMixedNames(final List<String> mixedNames) {
        return mixedNames.stream()
                .map(MixedName::from)
                .toList();
    }

    @Override
    public String toString() {
        return "Component{" +
                "id=" + id +
                ", type=" + type +
                ", summary=" + summary +
                ", commentCount=" + commentCount +
                ", bookmarkCount=" + bookmarkCount +
                ", designReferenceCount=" + designReferenceCount +
                ", viewCount=" + viewCount +
                '}';
    }
}
