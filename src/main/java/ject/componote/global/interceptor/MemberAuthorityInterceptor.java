package ject.componote.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ject.componote.domain.auth.error.ForbiddenJWTException;
import ject.componote.domain.auth.error.NotFoundJWTException;
import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.auth.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Component
public class MemberAuthorityInterceptor implements HandlerInterceptor {
    private final String authAttributeKey;

    public MemberAuthorityInterceptor(@Value("${auth.attribute-key}") final String authAttributeKey) {
        this.authAttributeKey = authAttributeKey;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        final HandlerMethod handlerMethod = (HandlerMethod) handler;
        if (hasUserAnnotation(handlerMethod)) {
            validateMemberAuthorization(request);
        }

        return true;
    }

    private boolean hasUserAnnotation(final HandlerMethod handlerMethod) {
        final User methodAnnotation = handlerMethod.getMethodAnnotation(User.class);
        if (methodAnnotation != null) {
            return true;
        }

        final User classAnnotation = handlerMethod.getBeanType().getAnnotation(User.class);
        return classAnnotation != null;
    }

    private void validateMemberAuthorization(final HttpServletRequest request) {
        final AuthPrincipal authPrincipal = (AuthPrincipal) request.getAttribute(authAttributeKey);
        if (Objects.isNull(authPrincipal)) {
            throw new NotFoundJWTException();
        }

        if (!authPrincipal.isMember()) {
            throw new ForbiddenJWTException();
        }
    }
}
