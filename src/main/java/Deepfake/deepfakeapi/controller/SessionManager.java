package Deepfake.deepfakeapi.controller;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {

    public static final String SESSION_COOKIE_NAME = "sessionId";
    private Map<String, Object> sessionStorage = new ConcurrentHashMap<>();
    
    /*
        세션 생성, 쿠키에 저장
     */
    public void createSession(Object value, HttpServletResponse response){
        // 세션 아이디 생성
        String sessionId = UUID.randomUUID().toString();
        sessionStorage.put(sessionId, value);

        // 쿠키 생성
        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        response.addCookie(cookie);
        
    }
    
    /*
        클라이언트 세션 확인
     */
    public Object getSession(HttpServletRequest request){
        Cookie cookie = findCookie(request, SESSION_COOKIE_NAME);
        if(cookie == null){
            return null;
        }
        return sessionStorage.get(cookie.getValue());
    }
    private Cookie findCookie(HttpServletRequest request, String cookieName){
        if(request.getCookies() == null){
            return null;
        }
        return Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(cookieName))
                .findAny().orElse(null);
    }
    
    /*
        쿠키 제거
     */
    public void expireCookie(HttpServletRequest request){
        Cookie cookie = findCookie(request, SESSION_COOKIE_NAME);
        if(cookie != null){
            sessionStorage.remove(cookie.getValue());
        }
    }
    
    

}
