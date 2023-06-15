package Deepfake.deepfakeapi.service;

import Deepfake.deepfakeapi.domain.Bbs;
import Deepfake.deepfakeapi.domain.Member;
import Deepfake.deepfakeapi.repository.BbsRepository;
import Deepfake.deepfakeapi.repository.FileRepository;
import Deepfake.deepfakeapi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BbsService {

    private final BbsRepository bbsRepository;
    private final MemberRepository memberRepository;
    private final FileRepository fileRepository;

    /**
     * 게시글 작성
     */
    @Transactional
    public Long create(String bbsTitle, String id, String bbsContent, Long fileId){

        //엔티티 조회
        Member member = memberRepository.findOne(id);

        //게시글 생성
        Bbs bbs = Bbs.createOne(bbsTitle, member, bbsContent);

        if(fileId != null){
            bbs.setFileId(fileId);
        }

        //게시글 저장
        bbsRepository.save(bbs);

        return bbs.getId();
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public Long update(Long bbsId, String bbsTitle, String bbsContent){

        Bbs bbs = bbsRepository.findOne(bbsId);

        bbs.setBbsTitle(bbsTitle);
        bbs.setBbsContent(bbsContent);

        return bbs.getId();

    }

    /**
     * 게시글 삭제
     */
    @Transactional
    public int delete(Long bbsId){
        Bbs bbs = bbsRepository.findOne(bbsId);
        if(bbs == null){
            return 0;
        }
        bbs.delete();
        return 1;
    }

    /**
     * 목록을 위해 게시글 모두 조회
     */
    public List<Bbs> findAll(int pageNumber){
        return bbsRepository.findAll(pageNumber);
    }

    /**
     * 게시판의 다음 페이지가 있는지
     */
    public boolean nextPage(int pageNumber){
        if(bbsRepository.findAll(pageNumber).size() == 0){
            return false;
        }
        return true;
    }

    /**
     * 하나의 게시글 보여주기
     */
    public Bbs getBbs(Long bbsId){
        Bbs bbs = bbsRepository.findOne(bbsId);
        if(bbs == null){
            throw new IllegalStateException("게시글을 불러올 수 없습니다.");
        }
        return bbs;
    }

    /**
     * 총 게시글 개수
     */
    public int count(){
        return bbsRepository.count();
    }

    public int getLastPage(){
        int lastPage = (int)Math.ceil((double)count() / 10);
        if(lastPage >= 1 ){
            return lastPage;
        }
        return 1;
    }

}
