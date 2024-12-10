package ject.componote.domain.common.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class PageResponse<T> {
    private final int totalCount;
    private final List<T> content;
    private final int pageNumber;
    private final int pageSize;

    public static <T> PageResponse<T> from(final Page<T> page) {
        return new PageResponse<>(page.getTotalPages(), page.getContent(), page.getNumber(), page.getSize());
    }
}
