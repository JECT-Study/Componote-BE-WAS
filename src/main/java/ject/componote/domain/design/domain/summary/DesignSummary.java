package ject.componote.domain.design.domain.summary;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import ject.componote.domain.common.model.BaseImage;
import ject.componote.domain.common.model.Count;
import ject.componote.domain.common.model.converter.BaseImageConverter;
import ject.componote.domain.common.model.converter.CountConverter;
import ject.componote.domain.design.model.DesignPhotoImage;
import ject.componote.domain.design.model.converter.DesignPhotoImageConverter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class DesignSummary {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "organization", nullable = false)
    private String organization;

    @Column(name = "description", nullable = false)
    private String description;

    @Convert(converter = DesignPhotoImageConverter.class)
    @Column(name = "thumbnail", nullable = false)
    private DesignPhotoImage thumbnail;

    @Convert(converter = CountConverter.class)
    @Column(name = "recommend_count", nullable = false)
    private Count recommendCount;

    private DesignSummary(final String name, final String organization, final String description, final DesignPhotoImage thumbnail, final Count recommendCount) {
        this.name = name;
        this.organization = organization;
        this.description = description;
        this.thumbnail = thumbnail;
        this.recommendCount = recommendCount;
    }

    public static DesignSummary of(final String name, final String organization, final String description, final DesignPhotoImage thumbnail, final Count recommendCount) {
        return new DesignSummary(name, organization, description, thumbnail, recommendCount);
    }
}
