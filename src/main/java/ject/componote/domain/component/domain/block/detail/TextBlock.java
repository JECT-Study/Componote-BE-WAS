package ject.componote.domain.component.domain.block.detail;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import ject.componote.domain.component.domain.block.BlockType;
import ject.componote.domain.component.domain.block.ContentBlock;
import ject.componote.domain.component.model.ComponentContent;
import ject.componote.domain.component.model.converter.ComponentContentConverter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@PrimaryKeyJoinColumn(name = "content_block_id")
@ToString
public class TextBlock extends ContentBlock {
    @Convert(converter = ComponentContentConverter.class)
    @Column(name = "content", nullable = false)
    private ComponentContent content;

    private TextBlock(final BlockType type, final ComponentContent content, final Integer order) {
        super(type, order);
        this.content = content;
    }

    public static TextBlock of(final BlockType type, final ComponentContent content, final Integer order) {
        return new TextBlock(type, content, order);
    }

    @Override
    public String getValue() {
        return content.getValue();
    }
}
