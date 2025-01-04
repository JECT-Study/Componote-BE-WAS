package ject.componote.domain.design.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import ject.componote.domain.design.model.DesignPhotoImage;
import ject.componote.domain.design.model.converter.DesignPhotoImageConverter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class DesignPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "design_id", nullable = false)
    private Long designId;

    @Convert(converter = DesignPhotoImageConverter.class)
    @Column(name = "image", nullable = false)
    private DesignPhotoImage image;

    public void setDesignId(final Long designId) {
        if (designId != null) {
            this.designId = designId;
        }
    }
}
