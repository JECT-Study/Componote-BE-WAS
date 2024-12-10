package ject.componote.domain.design.domain.link;

import lombok.Getter;

import java.util.List;

@Getter
public class DesignLinks {
    private final List<DesignLink> links;

    public DesignLinks(final List<DesignLink> links) {
        this.links = links;
    }
}
