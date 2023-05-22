package Deepfake.deepfakeapi.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class MemberJoinForm{

    @NotEmpty(message = "아이디는 필수입니다.")
    @Size(min = 5, max = 20)
    private String id;

    @NotEmpty(message = "회원 이름은 필수입니다.")
    private String name;
    @NotEmpty(message = "비밀번호는 필수입니다.")
    @Size(min= 5)
    private String password1;

    @NotEmpty(message = "비밀번호 검증은 필수입니다.")
    private String password2;

    @NotEmpty(message = "이메일은 필수입니다.")
    @Email
    private String email;
}
