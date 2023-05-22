package Deepfake.deepfakeapi.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;
    
    /*
        클라이언트 요청을 가로채어 jwt 인증을 진행
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request); // 헤더에서 JWT를 가져옴
       
        if (token != null && jwtTokenProvider.validateToken(token)) { // 해당 토큰이 유효한지 검사
            Authentication authentication = jwtTokenProvider.getAuthentication(token); // 토큰으로부터 사용자의 인증 정보를 가져옴

            SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContext 에 Authentication 객체를 저장
        }
        chain.doFilter(request, response);
    }
}
