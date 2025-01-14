package ject.componote.domain.bookmark.application;

import ject.componote.domain.auth.domain.Member;
import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.bookmark.dto.request.BookmarkRequest;
import ject.componote.domain.bookmark.dto.response.BookmarkResponse;
import ject.componote.domain.common.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.function.Predicate;

public enum BookmarkType {
    COMPONENT(
            request -> "component".equals(request.type()),
            BookmarkService::createComponentBookmark,
            BookmarkService::getComponentBookmarks,
            BookmarkService::deleteComponentBookmark
    ),
    DESIGN_SYSTEM(
            request -> "designSystem".equals(request.type()),
            BookmarkService::createDesignSystemBookmark,
            BookmarkService::getDesignSystemBookmarks,
            BookmarkService::deleteDesignSystemBookmark
    );

    private final Predicate<BookmarkRequest> condition;
    private final CreateBookmarkFunction createFunction;
    private final GetBookmarksFunction getFunction;
    private final DeleteBookmarkFunction deleteFunction;

    BookmarkType(
            Predicate<BookmarkRequest> condition,
            CreateBookmarkFunction createFunction,
            GetBookmarksFunction getFunction,
            DeleteBookmarkFunction deleteFunction
    ) {
        this.condition = condition;
        this.createFunction = createFunction;
        this.getFunction = getFunction;
        this.deleteFunction = deleteFunction;
    }

    public static BookmarkType from(String type) {
        return Arrays.stream(values())
                .filter(bookmarkType -> bookmarkType.condition.test(new BookmarkRequest(null, type)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid bookmark type: " + type));
    }

    public BookmarkResponse create(BookmarkService service, Member member, Long id) {
        return createFunction.create(service, member, id);
    }

    public PageResponse<BookmarkResponse> get(BookmarkService service, AuthPrincipal authPrincipal, Pageable pageable) {
        return getFunction.get(service, authPrincipal, pageable);
    }

    public BookmarkResponse delete(BookmarkService service, AuthPrincipal authPrincipal, Long id) {
        return deleteFunction.delete(service, authPrincipal, id);
    }

    @FunctionalInterface
    interface CreateBookmarkFunction {
        BookmarkResponse create(BookmarkService service, Member member, Long id);
    }

    @FunctionalInterface
    interface GetBookmarksFunction {
        PageResponse<BookmarkResponse> get(BookmarkService service, AuthPrincipal authPrincipal, Pageable pageable);
    }

    @FunctionalInterface
    interface DeleteBookmarkFunction {
        BookmarkResponse delete(BookmarkService service, AuthPrincipal authPrincipal, Long id);
    }
}