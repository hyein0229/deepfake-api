package Deepfake.deepfakeapi.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    // jwt 암호화에 사용할 비밀키 
    private String secretKey = "spring-messenger-key";
    
    // 토큰의 유효시간 30분 지정
    private long tokenValidTime = 30 * 60 * 1000L;
    private final UserDetailsService userDetailsService;
    
    // secretKey 인코딩
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /*
        JWT 토큰 생성
     */
    public String createToken(String userPk, String roles) {
        Claims claims = Jwts.claims().setSubject(userPk); // claim = JWT payload 에 저장되는 정보단위, 사용자를 식별하는 값을 저장
        claims.put("roles", roles); // 정보는 key-value 쌍으로 저장
        Date now = new Date(); // 현재 발급 시간
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발급 시간 정보
                .setExpiration(new Date(now.getTime() + tokenValidTime)) // 토큰의 만료시간 정보
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 암호화 알고리즘과 secret값으로 서명 생성
                .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 사용자 정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // 클라이언트 요청 헤더에서 토큰값을 가져옴
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");  // "Authorization" : "TOKEN값'
    }
    
    /*
        토큰의 유효성과 만료시간 확인
     */
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }














}
