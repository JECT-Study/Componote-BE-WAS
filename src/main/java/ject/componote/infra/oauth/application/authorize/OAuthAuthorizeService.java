package ject.componote.infra.oauth.application.authorize;

import ject.componote.infra.oauth.dto.authorize.response.OAuthAuthorizePayload;
import ject.componote.infra.oauth.model.OAuthProvider;
import ject.componote.infra.oauth.util.converter.OAuthParamConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class OAuthAuthorizeService {
    private final AuthorizeUriProvider authorizeUriProvider;
    private final OAuthParamConverter oAuthParamConverter;

    public OAuthAuthorizePayload getAuthorizePayload(final OAuthProvider oAuthProvider) {
        final MultiValueMap<String, String> queryParams = oAuthParamConverter.convertToAuthorizeParams(oAuthProvider);
        final URI authorizeEntryUri = authorizeUriProvider.provideEntryUri(oAuthProvider, queryParams);
        return OAuthAuthorizePayload.of(oAuthProvider, authorizeEntryUri);
    }
}
