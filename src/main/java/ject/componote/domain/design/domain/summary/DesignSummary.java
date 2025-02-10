package ject.componote.domain.design.domain.summary;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import ject.componote.domain.common.model.BaseImage;
import ject.componote.domain.common.model.Count;
import ject.componote.domain.common.model.converter.BaseImageConverter;
import ject.componote.domain.common.model.converter.CountConverter;
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

    @Convert(converter = BaseImageConverter.class)
    @Column(name = "thumbnail", nullable = false)
    private BaseImage thumbnail;

    @Convert(converter = CountConverter.class)
    @Column(name = "recommendCount", nullable = false)
    private Count recommendCount;

    private DesignSummary(final String name, final String organization, final String description, final BaseImage thumbnail, final Count recommendCount) {
        this.name = name;
        this.organization = organization;
        this.description = description;
        this.thumbnail = thumbnail;
        this.recommendCount = recommendCount;
    }

    public static DesignSummary of(final String name, final String organization, final String description, final BaseImage thumbnail, final Count recommendCount) {
        return new DesignSummary(name, organization, description, thumbnail, recommendCount);
    }
}
