package ject.componote.domain.component.domain.summary;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import ject.componote.domain.component.model.ComponentThumbnail;
import ject.componote.domain.component.model.converter.ComponentThumbnailConverter;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ComponentSummary {
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "introduction", nullable = false)
    private String introduction;

    @Convert(converter = ComponentThumbnailConverter.class)
    @Column(name = "thumbnail", nullable = false)
    private ComponentThumbnail thumbnail;

    private ComponentSummary(final String title, final String introduction, final ComponentThumbnail thumbnail) {
        this.title = title;
        this.introduction = introduction;
        this.thumbnail = thumbnail;
    }

    public static ComponentSummary of(final String title, final String introduction, final String thumbnailObjectKey) {
        return new ComponentSummary(title, introduction, ComponentThumbnail.from(thumbnailObjectKey));
    }
}
