package ject.componote.infra.storage.dto.move.request;

import ject.componote.domain.common.model.AbstractImage;

public record ImageMoveRequest(String tempObjectKey) {
    public static ImageMoveRequest from(final AbstractImage image) {
        return new ImageMoveRequest(image.getObjectKey());
    }
}
