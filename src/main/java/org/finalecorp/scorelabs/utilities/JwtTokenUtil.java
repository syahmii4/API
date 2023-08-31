package org.finalecorp.scorelabs.utilities;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.finalecorp.scorelabs.models.Users;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.naming.AuthenticationException;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtTokenUtil {
    private final SecretKey key;
    private final long accessTokenValidity = 60*60*1000;

    private final JwtParser jwtParser;

    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer";

    public JwtTokenUtil(){
        key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        this.jwtParser = Jwts.parser().setSigningKey(key);
    }

    public String createToken(Users user){
        Claims claims = Jwts.claims().setSubject(user.getUsername());

        claims.put("email", user.getEmailAddress());
        claims.put("fullName", user.getFullName());
        claims.put("role", user.getRole());
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(key)
                .compact();
    }

    private Claims parseJwtClaims(String token){
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public Claims resolveClaims(HttpServletRequest req){
        try{
            String token = resolveToken(req);
            if (token != null){
                return parseJwtClaims(token);
            }
            return null;
        } catch (ExpiredJwtException e){
            req.setAttribute("expired",e.getMessage());
            throw e;
        } catch (Exception e){
            req.setAttribute("invalid",e.getMessage());
            throw e;
        }
    }

    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)){
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean validateClaims(Claims claims)throws AuthenticationException {
        try {
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            throw e;
        }
    }

    public String getUsername(Claims claims) {
        return claims.getSubject();
    }

    public String getEmailAddress(Claims claims) {
        return (String) claims.get("email");
    }

    public String getFullName(Claims claims) {
        return (String) claims.get("fullName");
    }

    public int getRole(Claims claims) {
        return (int) claims.get("role");
    }
}
