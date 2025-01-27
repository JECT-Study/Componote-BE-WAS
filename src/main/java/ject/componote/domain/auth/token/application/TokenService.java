package ject.componote.domain.auth.token.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import ject.componote.domain.auth.domain.SocialAccount;
import ject.componote.domain.auth.error.InvalidJWTException;
import ject.componote.domain.auth.token.error.InvalidSocialAccountTokenException;
import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.auth.token.model.TokenProvider;
import ject.componote.domain.auth.token.model.TokenType;
import ject.componote.domain.auth.token.repository.InMemoryTokenProviderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenService {
    private final InMemoryTokenProviderRepository tokenProviderRepository;
    private final ObjectMapper objectMapper;

    public String createAccessToken(final AuthPrincipal authPrincipal) {
        return createToken(authPrincipal, TokenType.ACCESS);
    }

    public String createSocialAccountToken(final SocialAccount socialAccount) {
        return createToken(socialAccount.getId(), TokenType.SOCIAL_ACCOUNT);
    }

    public AuthPrincipal extractAccessTokenPayload(final String accessToken) {
        try {
            return objectMapper.readValue(
                    extractPayload(accessToken, TokenType.ACCESS),
                    AuthPrincipal.class
            );
        } catch (JsonProcessingException e) {
            throw new InvalidJWTException();
        }
    }

    public Long extractSocialAccountTokenPayload(final String socialAccountToken) {
        try {
            final String subject = extractPayload(socialAccountToken, TokenType.SOCIAL_ACCOUNT);
            return Long.valueOf(subject);
        } catch (NumberFormatException e) {
            throw new InvalidSocialAccountTokenException(socialAccountToken);
        }
    }

    public boolean validateAccessToken(final String accessToken) {
        return validateToken(accessToken, TokenType.ACCESS);
    }

    private <T> String createToken(final T payload, final TokenType type) {
        final TokenProvider provider = tokenProviderRepository.getProvider(type);
        final String subject = createSubject(payload);
        final Claims claims = Jwts.claims()
                .setSubject(subject);
        final Date now = new Date();
        final Date expirationDate = new Date(now.getTime() + provider.expiration());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(provider.key())
                .compact();
    }

    private String extractPayload(final String token, final TokenType type) {
        final TokenProvider provider = tokenProviderRepository.getProvider(type);
        return Jwts.parserBuilder()
                .setSigningKey(provider.key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private boolean validateToken(final String token, final TokenType type) {
        try {
            final TokenProvider provider = tokenProviderRepository.getProvider(type);
            final Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(provider.key())
                    .build()
                    .parseClaimsJws(token);
            return !claims.getBody()
                    .getExpiration()
                    .before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private <T> String createSubject(final T payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException();
        }
    }
}
