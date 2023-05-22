package Deepfake.deepfakeapi.service;

import Deepfake.deepfakeapi.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import Deepfake.deepfakeapi.repository.MemberRepository;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /*
        회원가입
     */
    @Transactional
    public String join(Member member){

        // 중복 회원 확인
        validateDuplicateId(member);
        validateDuplicateEmail(member);
        // 비밀번호 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberRepository.save(member);
        return member.getUserId();
    }

    /*
        로그인
     */
    public Member login(String inputId, String inputPasswd){

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Member findMember = memberRepository.findByUserId(inputId).get();
        String savedPW = findMember.getPassword();
        // 비밀번호 일치 확인
        if(passwordEncoder.matches(inputPasswd, savedPW)){
            return findMember;
        }
        else{
            return null;
        }
    }

    public void validateDuplicateId(Member member){

        Optional<Member> result = memberRepository.findByUserId(member.getUserId());
        if(!result.isEmpty()){
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
    }
    public void validateDuplicateEmail(Member member){

        Optional<Member> result = memberRepository.findByEmail(member.getEmail());
        if(!result.isEmpty()){
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }
    }
}
