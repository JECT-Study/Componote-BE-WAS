package ject.componote.domain.bookmark.application;

import ject.componote.domain.auth.domain.Member;
import ject.componote.domain.auth.domain.MemberRepository;
import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.bookmark.domain.Bookmark;
import ject.componote.domain.bookmark.domain.BookmarkRepository;
import ject.componote.domain.bookmark.dto.response.BookmarkResponse;
import ject.componote.domain.component.domain.Component;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;

    public BookmarkResponse addBookmark(AuthPrincipal authPrincipal, Long componentId) {
        if (bookmarkRepository.existsByUserIdAndComponentId(authPrincipal.id(), componentId)) {
            throw new IllegalArgumentException("이미 북마크에 추가된 컴포넌트입니다.");
        }

        Member member = memberRepository.findById(authPrincipal.id());
        Component component = componentRepository.findById(componentId);

        Bookmark bookmark = Bookmark.of(member, component);
        bookmarkRepository.save(bookmark);

        return BookmarkResponse.of(bookmark);
    }

    public BookmarkResponse getBookmark(AuthPrincipal authPrincipal) {
        // 사용자의 북마크 조회
        var bookmarks = bookmarkRepository.findAllByUserId(authPrincipal.getUserId());

        return BookmarkResponse.of(bookmarks);
    }

    public BookmarkResponse deleteBookmark(AuthPrincipal authPrincipal, Long componentId) {
        // 북마크 존재 여부 확인
        Bookmark bookmark = bookmarkRepository.findByUserIdAndComponentId(authPrincipal.getUserId(), componentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 북마크입니다."));

        // 북마크 삭제
        bookmarkRepository.delete(bookmark);

        return BookmarkResponse.of(bookmark);
    }
}
