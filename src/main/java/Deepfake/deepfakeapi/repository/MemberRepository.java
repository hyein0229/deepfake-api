package Deepfake.deepfakeapi.repository;

import Deepfake.deepfakeapi.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public Member save(Member member){
        em.persist(member);
        return member;
    }

    public Member findOne(String id){
        return em.find(Member.class, id);
    }

    public Optional<Member> findByUserId(String id){
        return em.createQuery("select m from Member m where m.userId = :id", Member.class)
                .setParameter("id", id)
                .getResultList().stream().findAny();
    }

    public Optional<Member> findByEmail(String email){
        return em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getResultList().stream().findAny();
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
}
