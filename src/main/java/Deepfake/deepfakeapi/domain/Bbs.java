package Deepfake.deepfakeapi.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Getter @Setter
public class Bbs {

    @Id
    @GeneratedValue
    @Column(name = "bbs_id")
    private Long id;

    private String bbsTitle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime date;

    private String bbsContent;

    private int bbsAvailable;

    // 업로드한 파일
    private Long fileId;

    //== 연관관계 메소드 ==//
    public void setMember(Member member){
        this.member = member;
        member.getBbs().add(this);
    }

    //== 생성 메소드 ==//
    public static Bbs createOne(String bbsTitle, Member member, String bbsContent){
        Bbs bbs = new Bbs();
        bbs.setBbsTitle(bbsTitle);
        bbs.setMember(member);
        bbs.setBbsContent(bbsContent);
        bbs.setBbsAvailable(1);
        bbs.setDate(LocalDateTime.now());
        return bbs;
    }

    //== 게시글 삭제 ==//
    public void delete(){
        setBbsAvailable(0);
    }

}
