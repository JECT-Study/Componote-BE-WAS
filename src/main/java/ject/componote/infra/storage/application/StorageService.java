package ject.componote.infra.storage.application;

import ject.componote.domain.common.model.AbstractImage;
import ject.componote.infra.storage.dto.move.request.ImageMoveRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StorageService {
    private final StorageProducer storageProducer;

    public void moveImage(final AbstractImage image) {
        if (image.isEmpty()) {
            log.warn("이미지가 비어 있습니다.");
            return;
        }

        storageProducer.sendImageMoveMessage(
                ImageMoveRequest.from(image)    // AbstractImage 의존성 발생
        );
    }
}
