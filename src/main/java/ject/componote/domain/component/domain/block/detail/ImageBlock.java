package ject.componote.domain.component.domain.block.detail;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import ject.componote.domain.common.model.BaseImage;
import ject.componote.domain.common.model.converter.BaseImageConverter;
import ject.componote.domain.component.domain.block.BlockType;
import ject.componote.domain.component.domain.block.ContentBlock;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ImageBlock extends ContentBlock {
    @Convert(converter = BaseImageConverter.class)
    @Column(name = "image", nullable = false)
    private BaseImage image;

    private ImageBlock(final BlockType type, final BaseImage image, final Integer order) {
        super(type, order);
        this.image = image;
    }

    public static ImageBlock of(final BlockType type, final BaseImage image, final Integer order) {
        return new ImageBlock(type, image, order);
    }
}