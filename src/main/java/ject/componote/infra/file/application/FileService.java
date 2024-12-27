package ject.componote.infra.file.application;

import ject.componote.domain.common.model.BaseImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.scheduler.Schedulers;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {
    private final FileClient fileClient;

    public void moveImage(final BaseImage image) {
        if (image.isEmpty()) {
            log.warn("No image to move");
            return;
        }

        final String objectKey = image.getObjectKey();
        fileClient.move(objectKey)
                .publishOn(Schedulers.boundedElastic())
                .subscribe();
    }
}
