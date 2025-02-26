package ject.componote.infra.oauth.error;

import ject.componote.infra.oauth.model.OAuthProvider;

public class UnsupportedProviderException extends OAuthClientException {
    public UnsupportedProviderException(final String providerName) {
        super(providerName + " 소셜 로그인은 지원하지 않습니다.");
    }

    public UnsupportedProviderException(final OAuthProvider oAuthProvider) {
        this(oAuthProvider.name());
    }
}
