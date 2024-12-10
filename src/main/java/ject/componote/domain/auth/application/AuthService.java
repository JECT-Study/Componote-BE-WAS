package ject.componote.domain.auth.application;

import ject.componote.domain.auth.domain.MemberRepository;
import ject.componote.domain.auth.dto.authorize.request.MemberAuthorizeRequest;
import ject.componote.domain.auth.dto.authorize.response.MemberAuthorizeResponse;
import ject.componote.domain.auth.dto.login.request.MemberLoginRequest;
import ject.componote.domain.auth.dto.login.response.MemberLoginResponse;
import ject.componote.domain.auth.dto.signup.request.MemberSignupRequest;
import ject.componote.domain.auth.dto.signup.response.MemberSignupResponse;
import ject.componote.domain.auth.util.TokenProvider;
import ject.componote.infra.oauth.application.OAuthClient;
import ject.componote.infra.oauth.dto.authorize.response.OAuthAuthorizePayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final OAuthClient oAuthClient;
    private final OAuthResultHandler oauthResultHandler;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    public MemberAuthorizeResponse authorize(final MemberAuthorizeRequest memberAuthorizeRequest) {
        final String providerName = memberAuthorizeRequest.providerName();
        final OAuthAuthorizePayload oAuthAuthorizePayload = oAuthClient.getAuthorizePayload(providerName);
        return MemberAuthorizeResponse.from(oAuthAuthorizePayload);
    }

    public MemberSignupResponse signup(final MemberSignupRequest memberSignupRequest) {
        return null;
    }

    public MemberLoginResponse login(final MemberLoginRequest memberLoginRequest) {
        return null;
//        final SocialAccount socialAccount = oAuthClient.getProfile(memberLoginRequest.providerType(), memberLoginRequest.code())
//                .publishOn(Schedulers.boundedElastic())
//                .map(oauthResultHandler::save)
//                .block();
//
//        final AuthPrincipal authPrincipal = AuthPrincipal.from(member);
//        final String accessToken = tokenProvider.createToken(authPrincipal);
//        return MemberLoginResponse.of(accessToken, member);
    }
}
