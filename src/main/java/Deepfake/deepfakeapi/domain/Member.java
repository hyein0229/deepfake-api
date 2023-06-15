package Deepfake.deepfakeapi.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "member")
    private List<Bbs> bbs = new ArrayList<>();


}
