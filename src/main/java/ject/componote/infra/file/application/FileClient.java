package ject.componote.infra.file.application;

import ject.componote.infra.file.dto.move.request.MoveRequest;
import ject.componote.infra.file.error.FileClientException;
import ject.componote.infra.file.error.FileServerErrorResponse;
import ject.componote.infra.util.TimeoutDecorator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class FileClient {
    private final int maxRetry;
    private final int timeout;
    private final HttpMethod method;
    private final String uri;
    private final TimeoutDecorator timeoutDecorator;
    private final WebClient webClient;

    public FileClient(@Value("${storage.max-retry}") final int maxRetry,
                      @Value("${storage.timeout}") final int timeout,
                      @Value("${storage.client.move.method}") final HttpMethod method,
                      @Value("${storage.client.move.uri}") final String uri,
                      final TimeoutDecorator timeoutDecorator,
                      final WebClient webClient) {
        this.maxRetry = maxRetry;
        this.timeout = timeout;
        this.method = method;
        this.uri = uri;
        this.timeoutDecorator = timeoutDecorator;
        this.webClient = webClient;
    }

    public Mono<Void> move(final String objectKey) {
        return timeoutDecorator.decorate(
                moveImage(objectKey),
                maxRetry,
                timeout
        );
    }

    private Mono<Void> moveImage(final String objectKey) {
        return webClient.method(method)
                .uri(uri)
                .bodyValue(MoveRequest.from(objectKey))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::handle4xxError)
                .onStatus(HttpStatusCode::is5xxServerError, this::handle5xxError)
                .toBodilessEntity()
                .then()
                .doOnSuccess(ignore -> handleSuccess(objectKey))
                .doOnError(error -> handleError(objectKey, error));
    }

    private Mono<? extends Throwable> handle5xxError(final ClientResponse clientResponse) {
        return clientResponse.bodyToMono(FileServerErrorResponse.class)
                .map(FileServerErrorResponse::getMessage)
                .map(IllegalStateException::new);
    }

    private Mono<? extends Throwable> handle4xxError(final ClientResponse clientResponse) {
        return clientResponse.bodyToMono(FileServerErrorResponse.class)
                .map(FileClientException::new);
    }

    private void handleSuccess(final String tempKey) {
        log.info("File moved successfully: {}", tempKey);
    }


    private void handleError(final String tempKey, final Throwable error) {
        log.error("Failed to move file from {}: {}", tempKey, error.getMessage());
    }
}
