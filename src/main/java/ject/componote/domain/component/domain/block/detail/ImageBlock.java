package ject.componote.domain.component.domain.block.detail;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import ject.componote.domain.component.domain.block.BlockType;
import ject.componote.domain.component.domain.block.ContentBlock;
import ject.componote.domain.component.model.ComponentImage;
import ject.componote.domain.component.model.converter.ComponentImageConverter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@PrimaryKeyJoinColumn(name = "content_block_id")
@ToString
public class ImageBlock extends ContentBlock {
    @Convert(converter = ComponentImageConverter.class)
    @Column(name = "image", nullable = false)
    private ComponentImage image;

    private ImageBlock(final BlockType type, final ComponentImage image, final Integer order) {
        super(type, order);
        this.image = image;
    }

    public static ImageBlock of(final BlockType type, final ComponentImage image, final Integer order) {
        return new ImageBlock(type, image, order);
    }

    @Override
    public String getValue() {
        return image.toUrl();
    }
}