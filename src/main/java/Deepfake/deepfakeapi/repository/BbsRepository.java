package Deepfake.deepfakeapi.repository;

import Deepfake.deepfakeapi.domain.Bbs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BbsRepository {

    private final EntityManager em;

    public void save(Bbs bbs){
        if(bbs.getId() == null){
            em.persist(bbs);
        }else{
            em.merge(bbs);
        }
    }

    public Bbs findOne(Long id){
        return em.find(Bbs.class, id);
    }

    public List<Bbs> findAll(int pageNumber){
        int startNum = (pageNumber - 1) * 10;
        return em.createQuery("select b from Bbs b where b.bbsAvailable = 1 order by b.id desc", Bbs.class)
                .setFirstResult(startNum)
                .setMaxResults(10)
                .getResultList();
    }

    public int count(){
        return em.createQuery("select b from Bbs b where b.bbsAvailable = 1", Bbs.class)
                .getResultList()
                .size();

    }
}
