package ject.componote.domain.component.dto.find.response;

import ject.componote.domain.component.domain.block.BlockType;

import java.util.List;
import java.util.Map;

public record ComponentDetailResponse(
        String title,
        List<String> mixedNames,
        String introduction,
        Long commentCount,
        Long bookmarkCount,
        Long designReferenceCount,
        String thumbnailUrl,
        Map<BlockType, List<ComponentBlockResponse>> blocks,
        Boolean isBookmarked
) {

}
