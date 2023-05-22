package Deepfake.deepfakeapi.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="member")
@Getter @Setter
public class Member {

    public enum Role {
        USER, ADMIN
    }

    @Id
    @GeneratedValue
    private Long id;

    private String userId;

    private String name;

    private String password;

    private String email;

    private Role role;


}
