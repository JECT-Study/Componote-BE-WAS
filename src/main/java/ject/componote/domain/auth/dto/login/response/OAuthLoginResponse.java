package ject.componote.domain.auth.dto.login.response;

public record OAuthLoginResponse(boolean isRegister, String encryptedSocialAccountId) {
    public static OAuthLoginResponse of(final boolean isRegister, final String encryptedSocialAccountId) {
        return new OAuthLoginResponse(isRegister, encryptedSocialAccountId);
    }
}
