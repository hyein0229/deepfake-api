package Deepfake.deepfakeapi.controller;

import Deepfake.deepfakeapi.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import Deepfake.deepfakeapi.service.MemberService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final SessionManager sessionManager;

    /*
        회원가입
     */
    @PostMapping("/members/join")
    public ResponseEntity join(@RequestBody @Valid MemberJoinForm memberJoinForm, BindingResult result){

        // memberJoinForm 항목 유효성 체크
        if(result.hasErrors()){
            return new ResponseEntity<>(memberJoinForm,null, HttpStatus.BAD_REQUEST);
        }

        // 비밀번호 확인 일치한지 확인
        if(!memberJoinForm.getPassword1().equals(memberJoinForm.getPassword2())){
            result.rejectValue("password2", "passwordInCorrect", "비밀번호가 일치하지 않습니다.");
            return ResponseEntity.badRequest().build();
        }

        // 회원 생성, 회원가입 진행
        Member member = new Member();
        member.setUserId(memberJoinForm.getId());
        member.setName(memberJoinForm.getName());
        member.setPassword(memberJoinForm.getPassword1());
        member.setEmail(memberJoinForm.getEmail());
        member.setRole(Member.Role.USER);
        memberService.join(member);

        return ResponseEntity.ok().body(member.getId());
    }

    /*
        회원 로그인
     */
    @PostMapping("/members/Login")
    public ResponseEntity<?> Login(@RequestBody @Valid MemberLoginForm memberLoginForm, BindingResult result, HttpServletResponse httpServletResponse){

        if(result.hasErrors()){
            return new ResponseEntity<>(memberLoginForm,null,HttpStatus.OK);
        }
        // 존재하는 회원 중 아이디, 비밀번호 확인
        Member findMember = memberService.login(memberLoginForm.getId(), memberLoginForm.getPassword1());

        // 존재하는 회원이면 쿠키 세션 생성
        if(findMember != null){
//            sessionManager.createSession(findMember, httpServletResponse);
            Cookie cookie = new Cookie("memberCode", String.valueOf(findMember.getId()));
            cookie.setPath("/");
            httpServletResponse.addCookie(cookie);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /*
        로그아웃
     */
    @PostMapping("/members/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        expireCookie(response, "memberCode");
        return new ResponseEntity<>(HttpStatus.OK);
    }
    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        // 수명을 0으로 지정하여 쿠키 파기
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
