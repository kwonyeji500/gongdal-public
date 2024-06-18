package com.example.gongdal.config.jwt;

import com.example.gongdal.config.exception.code.ErrorResponseCode;
import com.example.gongdal.config.exception.error.CustomRuntimeException;
import com.example.gongdal.dto.token.TokenCommand;
import com.example.gongdal.dto.token.TokenRenewCommand;
import com.example.gongdal.entity.user.User;
import com.example.gongdal.repository.user.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expire.access}")
    private long expireAccess;
    @Value("${jwt.expire.refresh}")
    private long expireRefresh;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createAccessToken(String userId) {
        return createToken(userId, expireAccess);
    }

    public String createRefreshToken(String userId) {
        return createToken(userId, expireRefresh);
    }

    private String createToken(String userId, long expireTimeMillis) {
        Claims claims = Jwts.claims().setSubject(userId);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expireTimeMillis))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
    }

    public String getUserPk(String token) {
        String pk = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody()
                .getSubject();
        return pk;
    }

    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration()
                    .before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public TokenCommand renewAccessToken(TokenRenewCommand command) {
        TokenCommand token = resolveToken(command);

        assert token != null;
        if (validateToken(token.getAccessToken())) {
            throw new CustomRuntimeException(ErrorResponseCode.USER_TOKEN_VALIDA);
        }

        String userId = getUserPk(token.getRefreshToken());

        User user = userRepository.findById(Long.parseLong(userId)).orElseThrow(
                () -> new CustomRuntimeException(ErrorResponseCode.NOT_FOUND_USER));

        if (validateToken(token.getRefreshToken())) {
            return TokenCommand.renew(createAccessToken(userId), token.getRefreshToken(), user);
        } else {
            return TokenCommand.renew(createAccessToken(userId), createRefreshToken(userId), user);
        }
    }

    private TokenCommand resolveToken(TokenRenewCommand command) {
        String accessToken = command.getRequest().getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer")) {
            return TokenCommand.toCommand(accessToken.substring(7), command.getRefreshToken());
        }
        return null;
    }
}

