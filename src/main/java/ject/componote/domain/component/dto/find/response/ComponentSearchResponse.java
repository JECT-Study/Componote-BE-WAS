package ject.componote.domain.component.dto.find.response;

import java.util.List;

public record ComponentSearchResponse(Long id, String title, List<String> mixedNames) {
}
