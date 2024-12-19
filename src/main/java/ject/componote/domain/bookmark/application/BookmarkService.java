package ject.componote.domain.bookmark.application;

import ject.componote.domain.auth.domain.Member;
import ject.componote.domain.auth.domain.MemberRepository;
import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.bookmark.domain.Bookmark;
import ject.componote.domain.bookmark.domain.BookmarkRepository;
import ject.componote.domain.bookmark.dto.response.BookmarkResponse;
import ject.componote.domain.common.dto.response.PageResponse;
import ject.componote.domain.component.domain.Component;
import ject.componote.domain.component.domain.ComponentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final ComponentRepository componentRepository;

    public BookmarkResponse addBookmark(AuthPrincipal authPrincipal, Long componentId) {
        if (bookmarkRepository.existsByUserIdAndComponentId(authPrincipal.id(), componentId)) {
            throw new IllegalArgumentException("이미 북마크에 추가된 컴포넌트입니다.");
        }

        Member member = memberRepository.findById(authPrincipal.id())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));
        Component component = componentRepository.findById(componentId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 컴포넌트입니다."));

        Bookmark bookmark = Bookmark.of(member, component);
        bookmarkRepository.save(bookmark);

        return BookmarkResponse.of(bookmark);
    }

    @Transactional(readOnly = true)
    public PageResponse<BookmarkResponse> getBookmark(AuthPrincipal authPrincipal, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Bookmark> bookmarkPage = bookmarkRepository.findAllByUserId(authPrincipal.id(), pageable);

        Page<BookmarkResponse> bookmarkResponsePage = bookmarkPage.map(bookmark ->
            new BookmarkResponse(bookmark.getId())
        );

        return PageResponse.from(bookmarkResponsePage);
    }

    public BookmarkResponse deleteBookmark(AuthPrincipal authPrincipal, Long componentId) {
        Bookmark bookmark = bookmarkRepository.findByUserIdAndComponentId(authPrincipal.id(), componentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 북마크입니다."));

        bookmarkRepository.delete(bookmark);

        return BookmarkResponse.of(bookmark);
    }
}
