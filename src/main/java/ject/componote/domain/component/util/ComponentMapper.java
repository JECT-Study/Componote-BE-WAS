package ject.componote.domain.component.util;

import ject.componote.domain.component.domain.Component;
import ject.componote.domain.component.domain.MixedName;
import ject.componote.domain.component.domain.block.BlockType;
import ject.componote.domain.component.domain.block.ContentBlock;
import ject.componote.domain.component.domain.summary.ComponentSummary;
import ject.componote.domain.component.dto.find.response.ComponentBlockResponse;
import ject.componote.domain.component.dto.find.response.ComponentDetailResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@org.springframework.stereotype.Component
public class ComponentMapper {
    public ComponentDetailResponse mapFrom(final Component component, final Boolean isBookmarked) {
        final ComponentSummary summary = component.getSummary();
        return new ComponentDetailResponse(
                summary.getTitle(),
                parseMixedNames(component),
                summary.getDescription(),
                component.getCommentCount().getValue(),
                component.getBookmarkCount().getValue(),
                component.getDesignReferenceCount().getValue(),
                summary.getThumbnail().getImage().toUrl(),
                parseBlocks(component),
                isBookmarked
        );
    }

    private List<String> parseMixedNames(final Component component) {
        return component.getMixedNames()
                .stream()
                .map(MixedName::getName)
                .toList();
    }

    private Map<BlockType, List<ComponentBlockResponse>> parseBlocks(final Component component) {
        final List<ContentBlock> contentBlocks = component.getContentBlocks();
        return contentBlocks.stream()
                .collect(Collectors.groupingBy(
                        ContentBlock::getType,
                        Collectors.mapping(ComponentBlockResponse::from, Collectors.toList()))
                );
    }
}
