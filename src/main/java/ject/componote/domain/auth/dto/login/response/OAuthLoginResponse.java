package ject.componote.domain.auth.dto.login.response;

public record OAuthLoginResponse(boolean isRegister, String socialAccountToken) {
    public static OAuthLoginResponse of(final boolean isRegister, final String socialAccountToken) {
        return new OAuthLoginResponse(isRegister, socialAccountToken);
    }
}
