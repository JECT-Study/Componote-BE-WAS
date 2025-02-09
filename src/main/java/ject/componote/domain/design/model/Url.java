package ject.componote.domain.design.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

@Getter
@EqualsAndHashCode
@ToString
public class Url {
    private static final String URL_REGEX =
                    "^(https?:\\/\\/)" +                // 반드시 http 또는 https로 시작
                    // "(([a-zA-Z0-9\\-]+\\.)+[a-zA-Z]{2,})" + // 도메인 이름 (예: www.example.com)
                    "(\\:\\d+)?(\\/[-a-zA-Z0-9@:%._\\+~#=]*)*" + // 포트 및 경로 (선택적)
                    "(\\?[;&a-zA-Z0-9@:%_\\+.~#?&//=]*)?" +      // 쿼리 문자열 (선택적)
                    "(\\#[-a-zA-Z0-9@:%_\\+.~#?&//=]*)?$";       // 앵커 태그 (선택적)

    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

    private final String value;

    private Url(final String value) {
        validate(value);
        this.value = value;
    }

    public static Url from(final String value) {
        return new Url(value);
    }

    private void validate(final String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("URL cannot be null or empty");
        }

        try {
            new URL(value);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL format: " + value);
        }
    }
}
