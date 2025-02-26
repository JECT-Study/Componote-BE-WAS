package ject.componote.global.resolver;

import jakarta.servlet.http.HttpServletRequest;
import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.auth.model.Authenticated;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthPrincipalResolver implements HandlerMethodArgumentResolver {
    private final String authAttributeKey;

    public AuthPrincipalResolver(@Value("${auth.token.access.attribute-key}") final String authAttributeKey) {
        this.authAttributeKey = authAttributeKey;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().equals(AuthPrincipal.class)
                && parameter.hasParameterAnnotation(Authenticated.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter,
                                  final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest,
                                  final WebDataBinderFactory binderFactory) throws Exception {
        final HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        if (httpServletRequest.getAttribute(authAttributeKey) == null) {
            return null;
        }

        return httpServletRequest.getAttribute(authAttributeKey);
    }
}
