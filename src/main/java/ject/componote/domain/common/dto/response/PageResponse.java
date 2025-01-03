package ject.componote.domain.common.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@Getter
@ToString
public class PageResponse<T> {
    private final boolean hasNext;
    private final long totalElements;
    private final int totalPages;
    private final List<T> content;
    private final int pageNumber;
    private final int pageSize;

    public static <T> PageResponse<T> from(final Page<T> page) {
        return new PageResponse<>(page.hasNext(), page.getTotalElements(), page.getTotalPages(), page.getContent(), page.getNumber(), page.getSize());
    }
}
