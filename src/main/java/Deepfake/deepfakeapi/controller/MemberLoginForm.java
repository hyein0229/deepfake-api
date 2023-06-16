package Deepfake.deepfakeapi.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class MemberLoginForm {

    @NotEmpty(message = "아이디는 필수입니다.")
    @Size(min = 5, max = 20)
    private String id;

    @NotEmpty(message = "비밀번호는 필수입니다.")
    @Size(min = 5 )
    private String password1;
}
