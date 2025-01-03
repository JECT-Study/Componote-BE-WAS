package ject.componote.infra.file.dto.move.request;

public record MoveRequest(String objectKey) {
    public static MoveRequest from(final String objectKey) {
        return new MoveRequest(objectKey);
    }
}
