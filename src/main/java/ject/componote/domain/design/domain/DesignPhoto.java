package ject.componote.domain.design.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import ject.componote.domain.common.model.Image;
import ject.componote.domain.common.model.converter.ImageConverter;
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

    @Convert(converter = ImageConverter.class)
    @Column(name = "photo", nullable = false)
    private Image photo;

    public void setDesignId(final Long designId) {
        if (designId != null) {
            this.designId = designId;
        }
    }
}
