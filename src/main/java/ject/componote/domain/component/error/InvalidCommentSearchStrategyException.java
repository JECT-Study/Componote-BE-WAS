package ject.componote.domain.component.error;

import org.springframework.http.HttpStatus;

public class InvalidCommentSearchStrategyException extends ComponentException {
    public InvalidCommentSearchStrategyException() {
        super("컴포넌트 검색에 실패했습니다. 요청 Body를 확인해주세요.", HttpStatus.BAD_REQUEST);
    }
}
