package Deepfake.deepfakeapi.repository;

import Deepfake.deepfakeapi.domain.FileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class FileRepository {

    private final EntityManager em;

    public void save(FileEntity file){
        if(file.getId() == null){
            em.persist(file);
        }else{
            em.merge(file);
        }
    }

    public FileEntity findOne(Long id){
        return em.find(FileEntity.class, id);
    }
}
