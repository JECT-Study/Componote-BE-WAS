package ject.componote.domain.comment.application;

import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.comment.domain.CommentRepository;
import ject.componote.domain.comment.dto.create.request.CommentCreateRequest;
import ject.componote.domain.comment.dto.create.response.CommentCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    // 비동기로?
    public CommentCreateResponse create(final AuthPrincipal authPrincipal,
                                        final CommentCreateRequest request) {
        return null;
    }
}
