package ject.componote.domain.comment.application;

import ject.componote.domain.comment.dto.image.event.CommentImageMoveEvent;
import ject.componote.infra.storage.application.StorageProducer;
import ject.componote.infra.storage.dto.move.request.ImageMoveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class CommentImageEventListener {
    private final StorageProducer storageProducer;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleImageMove(final CommentImageMoveEvent event) {
        storageProducer.sendImageMoveMessage(ImageMoveRequest.from(event.image()));
    }
}
