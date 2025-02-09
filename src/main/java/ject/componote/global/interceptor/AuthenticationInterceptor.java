package ject.componote.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ject.componote.domain.auth.dao.MemberRepository;
import ject.componote.domain.auth.error.ExpiredJWTException;
import ject.componote.domain.auth.error.InvalidJWTException;
import ject.componote.domain.auth.error.NotFoundJWTException;
import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.auth.token.application.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {
    private static final String BEARER_TYPE = "Bearer";

    private final String authAttributeKey;
    private final MemberRepository memberRepository;
    private final TokenService tokenService;

    public AuthenticationInterceptor(@Value("${auth.token.access.attribute-key}") final String authAttributeKey,
                                     final MemberRepository memberRepository,
                                     final TokenService tokenService) {
        this.authAttributeKey = authAttributeKey;
        this.memberRepository = memberRepository;
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) throws Exception {
        if (isEmptyAuthHeader(request)) {
            return true;
        }

        final String accessToken = getAccessToken(request);
        validateToken(accessToken);

        AuthPrincipal authPrincipal = getAuthPrincipal(accessToken);
        validateAuthentication(authPrincipal);

        request.setAttribute(authAttributeKey, authPrincipal);

        return true;
    }

    private boolean isEmptyAuthHeader(final HttpServletRequest request) {
        final String headerValue = request.getHeader(AUTHORIZATION);
        return headerValue == null || headerValue.isEmpty();
    }

    private String getAccessToken(final HttpServletRequest request) {
        final String value = request.getHeader(AUTHORIZATION);
        if (value == null || value.isEmpty()) {
            throw new NotFoundJWTException();
        }

        if (!value.startsWith(BEARER_TYPE)) {
            throw new InvalidJWTException();
        }

        return value.substring(BEARER_TYPE.length())
                .trim();
    }

    private void validateToken(final String accessToken) {
        if (!tokenService.validateAccessToken(accessToken)) {
            throw new ExpiredJWTException();
        }
    }

    private void validateAuthentication(final AuthPrincipal authPrincipal) {
        final Long id = authPrincipal.id();
        if (!memberRepository.existsById(id)) {
            throw new InvalidJWTException();
        }
    }

    private AuthPrincipal getAuthPrincipal(final String accessToken) {
        return tokenService.extractAccessTokenPayload(accessToken);
    }
}
