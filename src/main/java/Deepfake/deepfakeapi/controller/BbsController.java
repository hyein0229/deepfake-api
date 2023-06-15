package Deepfake.deepfakeapi.controller;

import Deepfake.deepfakeapi.domain.Bbs;
import Deepfake.deepfakeapi.domain.FileEntity;
import Deepfake.deepfakeapi.domain.Member;
import Deepfake.deepfakeapi.service.BbsService;
import Deepfake.deepfakeapi.service.FileService;
import Deepfake.deepfakeapi.utility.MD5Generator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BbsController {
    private final BbsService bbsService;
    private final FileService fileService;

    @GetMapping("/bbs")
    public ResponseEntity<?> bbsMain(){

        int lastPage = bbsService.getLastPage(); // 모든 페이지 수
        Boolean nextPage = bbsService.nextPage(2); // 다음 페이지 2 페이지가 있는지 확인
        List<Bbs> bbs = bbsService.findAll(1); // 1페이지의 모든 게시판 글 가져오기

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/bbs/page")
    public ResponseEntity bbs(@RequestParam("pageNumber") int pageNumber){
        int lastPage = bbsService.getLastPage();
        Boolean nextPage = bbsService.nextPage(pageNumber + 1);
        List<Bbs> bbs = bbsService.findAll(pageNumber);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PostMapping("/bbs/create")
    public ResponseEntity create(@RequestParam("content") MultipartFile files){

        if(!files.isEmpty()){
            try {
                String origFileName = files.getOriginalFilename();
                String filename = new MD5Generator(origFileName).toString();
                String savedPath = System.getProperty("user.dir") + "\\files";
                if(!new java.io.File(savedPath).exists()){
                    try{
                        new File(savedPath).mkdir();
                    }catch(Exception e){
                        e.getStackTrace();
                    }
                }

                String filePath = savedPath + "\\" + filename;
                files.transferTo(new File(filePath));

                FileEntity file = new FileEntity();
                file.setOrigFilename(origFileName);
                file.setStoredFilename(filename);
                file.setFilePath(filePath);

                Long fileId = fileService.saveFile(file);

            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return new ResponseEntity<>(HttpStatus.OK);

    }



}
