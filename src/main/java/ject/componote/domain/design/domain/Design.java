package ject.componote.domain.design.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import ject.componote.domain.common.domain.BaseEntity;
import ject.componote.domain.common.model.BaseImage;
import ject.componote.domain.common.model.Count;
import ject.componote.domain.common.model.converter.CountConverter;
import ject.componote.domain.design.domain.summary.DesignSummary;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Design extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private DesignSummary summary;

    @Convert(converter = CountConverter.class)
    @Column(name = "bookmark_count", nullable = false)
    private Count bookmarkCount;

    private Design(final DesignSummary summary) {
        this.summary = summary;
        this.bookmarkCount = Count.create();
    }

    public static Design of(final String name, final String organization, final String description, final BaseImage thumbnail, final Count recommendCount) {
        return new Design(DesignSummary.of(name, organization, description, thumbnail, recommendCount));
    }
}
