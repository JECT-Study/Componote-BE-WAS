package ject.componote.infra.storage.application;

import ject.componote.infra.storage.dto.move.request.ImageMoveRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StorageProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendImageMoveMessage(final ImageMoveRequest request) {
        rabbitTemplate.convertAndSend("was-storage", "storage.image.move", request);
    }
}
