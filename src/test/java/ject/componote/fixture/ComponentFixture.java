package ject.componote.fixture;

import ject.componote.domain.component.domain.Component;
import ject.componote.domain.component.domain.ComponentType;

import java.util.Collections;
import java.util.List;

public enum ComponentFixture {
    INPUT_COMPONENT("input title", "input description", "objectKey.jpg", ComponentType.INPUT, List.of("hello"));

    private final String title;
    private final String description;
    private final String thumbnailObjectKey;
    private final ComponentType type;
    private final List<String> mixedNames;

    ComponentFixture(final String title, final String description, final String thumbnailObjectKey, final ComponentType type, final List<String> mixedNames) {
        this.title = title;
        this.description = description;
        this.thumbnailObjectKey = thumbnailObjectKey;
        this.type = type;
        this.mixedNames = mixedNames;
    }

    public Component 생성() {
        return Component.of(title, description, thumbnailObjectKey, type, mixedNames, Collections.emptyList());
    }
}
