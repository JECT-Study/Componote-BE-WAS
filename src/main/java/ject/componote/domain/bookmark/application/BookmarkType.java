package ject.componote.domain.bookmark.application;

import java.util.Arrays;

public enum BookmarkType {
    COMPONENT(new ComponentBookmarkStrategy()),
    DESIGN_SYSTEM(new DesignSystemBookmarkStrategy());

    private final BookmarkStrategy strategy;

    BookmarkType(BookmarkStrategy strategy) {
        this.strategy = strategy;
    }

    public BookmarkStrategy getStrategy() {
        return strategy;
    }

    public static BookmarkType from(String type) {
        return Arrays.stream(values())
                .filter(bookmarkType -> bookmarkType.name().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid bookmark type: " + type));
    }
}