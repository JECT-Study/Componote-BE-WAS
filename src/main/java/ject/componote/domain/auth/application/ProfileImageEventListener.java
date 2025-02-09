package ject.componote.domain.auth.application;

import ject.componote.domain.auth.dto.image.event.ProfileImageMoveEvent;
import ject.componote.domain.auth.model.ProfileImage;
import ject.componote.infra.storage.application.StorageProducer;
import ject.componote.infra.storage.dto.move.request.ImageMoveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ProfileImageEventListener {
    private final StorageProducer storageProducer;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleImageMove(final ProfileImageMoveEvent event) {
        final ProfileImage image = event.image();
        if (image.isDefaultImage()) {
            return;
        }

        storageProducer.sendImageMoveMessage(ImageMoveRequest.from(image));
    }
}
