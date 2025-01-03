package ject.componote.domain.component.dto.find.response;

import ject.componote.domain.component.domain.block.ContentBlock;

public record ComponentBlockResponse(Integer order, String content) {
    public static ComponentBlockResponse from(final ContentBlock contentBlock) {
        return new ComponentBlockResponse(
                contentBlock.getOrder(),
                contentBlock.getValue()
        );
    }
}
