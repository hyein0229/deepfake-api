package Deepfake.deepfakeapi.controller;

import Deepfake.deepfakeapi.domain.FileEntity;
import Deepfake.deepfakeapi.service.FileService;
import Deepfake.deepfakeapi.utility.MD5Generator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.util.ExceptionUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLContext;
import javax.servlet.http.Cookie;
import javax.swing.text.html.HTML;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    /*
        파일 업로드
     */
//    @PostMapping("/upload")
//    public ResponseEntity<?> upload(@CookieValue("memberCode") Cookie cookie, @RequestParam("file") MultipartFile files) throws Exception{
//
//        // cookie 값 받아졌는지 로그 확인
//        log.info(cookie.getValue());
//
//        // 파일이 존재하면
//        if(!files.isEmpty()){
//            try {
////                // 딥페이크 검출 진행
//                //deepFake(cookie, files);
////                if(isDeepfake){
////                    return new ResponseEntity<>("deepfake image", HttpStatus.OK);
////                }
//
//                // 기존 파일이름을 다른 파일 이름으로 저장하기 위해 변환
//                String origFileName = files.getOriginalFilename();
//                String filename = new MD5Generator(origFileName).toString();
//
//                // 서버에 저장될 위치
//                String savedPath = System.getProperty("user.dir") + "\\files";
//                if(!new java.io.File(savedPath).exists()){
//                    try{
//                        new File(savedPath).mkdir();
//                    }catch(Exception e){
//                        e.getStackTrace();
//                    }
//                }
//                String filePath = savedPath + "\\" + filename;
//                files.transferTo(new File(filePath));
//
//                // DB에 저장될 파일 정보 객체 생성
//                FileEntity file = new FileEntity();
//                file.setOrigFilename(origFileName);
//                file.setStoredFilename(filename);
//                file.setFilePath(filePath);
//
//                fileService.saveFile(file);
//
//                return new ResponseEntity<>("Success", HttpStatus.OK);
//
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//        }
//        return new ResponseEntity<>("File not exists", HttpStatus.OK);
//    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("content") String html) throws Exception{

            System.out.println(html);
            ArrayList<String> imgArray = new ArrayList<>(); // html 에서 parsing한 이미지 파일 이름 리스트
            
            // html 파일로부터 img Tag 만 추출하기
            Document doc = Jsoup.parse(html);
            Elements imgs = doc.getElementsByTag("img");
            if(imgs.size() > 0){
                for(Element img : imgs){
                    String src = img.attr("src"); // img 태그의 src 속성
                    String[] splited = src.split("/"); // http://localhost:9999/img/example.png
                    String imgName = splited[splited.length - 1]; // example.png 추출
                    imgArray.add(imgName);
                }
            }
            
            // html 파일로부터 video만 추출
            Elements videos = doc.getElementsByTag("iframe");
            if(videos.size() > 0){
                
            }
            
            // 실제 이미지 파일 가져오기
            ArrayList<File> imgFileArray = new ArrayList<>();
            if(!imgArray.isEmpty()){ // 추출된 img가 존재하는지 확인
                for(String img : imgArray){
                    // 서버에 저장된 경로에서 파일 가져오기
                    File file = new File("C:/Users/HAYOUNG LEE/Desktop/deepfake-api/files/" + img);
                    System.out.println(file);
                    imgFileArray.add(file);
                }
            }

            /*
                동영상의 경우 이미지 추출
             */
//            VideoConvertor videoConvertor = new VideoConvertor();
//            File videoSource = new File("mp4");
//            videoConvertor.getImageFrames(videoSource);

            /*
                deepfake API 로 이미지 파일들을 보내어 딥페이크 탐지 구현
             */

//        // 파일이 존재하면
//        if(!imgFileArray.isEmpty()){
//            try {
////                // 딥페이크 검출 진행
//                //deepFake(cookie, files);
////                if(isDeepfake){
////                    return new ResponseEntity<>("deepfake image", HttpStatus.OK);
////                }
//                // 기존 파일이름을 다른 파일 이름으로 저장하기 위해 변환
//                String origFileName = files.getOriginalFilename();
//                String filename = new MD5Generator(origFileName).toString();
//
//                // 서버에 저장될 위치
//                String savedPath = System.getProperty("user.dir") + "\\files";
//                if(!new java.io.File(savedPath).exists()){
//                    try{
//                        new File(savedPath).mkdir();
//                    }catch(Exception e){
//                        e.getStackTrace();
//                    }
//                }
//                String filePath = savedPath + "\\" + filename;
//                files.transferTo(new File(filePath));
//
//                // DB에 저장될 파일 정보 객체 생성
//                FileEntity file = new FileEntity();
//                file.setOrigFilename(origFileName);
//                file.setStoredFilename(filename);
//                file.setFilePath(filePath);
//
//                fileService.saveFile(file);
//
//                return new ResponseEntity<>("Success", HttpStatus.OK);
//
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    /*
        OK 버튼 클릭하여 이미지 삽입 시 서버로 업도드되어 저장 ( 글 등록과 다름 )
     */
    @PostMapping("/image")
    public ResponseEntity<?> image(@RequestParam("image") MultipartFile file) throws Exception{
        System.out.println("image controller success");
        String origFileName = file.getOriginalFilename();
        String filename = new MD5Generator(origFileName).toString();

        // 서버에 저장될 위치
        String savedPath = System.getProperty("user.dir") + "\\files";
        if(!new java.io.File(savedPath).exists()){
            try{
                new File(savedPath).mkdir();
            }catch(Exception e){
                e.getStackTrace();
            }
        }
        String filePath = savedPath + "\\" + filename + ".png";
        file.transferTo(new File(filePath));

//        // DB에 저장될 파일 정보 객체 생성
        FileEntity savedFile = new FileEntity();
        savedFile.setOrigFilename(origFileName);
        savedFile.setStoredFilename(filename);
        savedFile.setFilePath(filePath);
        fileService.saveFile(savedFile);

        return new ResponseEntity<>(filename + ".png", HttpStatus.OK);
    }
//
//    private void deepFake(Cookie cookie, MultipartFile file){
//
//        try {
//            // http client builder
//            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
//
//            // 모든 인증서를 신뢰하도록 설정
//            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (X509Certificate[] chain, String authType) -> true).build();
//            httpClientBuilder.setSSLContext(sslContext);
//
//            // Https 인증 요청시 호스트네임 유효성 검사를 진행하지 않게 한다.
//            SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
//            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
//                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
//                    .register("https", sslSocketFactory).build();
//
//            PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
//            httpClientBuilder.setConnectionManager(connMgr); // http client builder 에 설정
//
//            // build 하여 httpclient 생성, RestTemplate 와 HttpClient 연결
//            HttpClient client = httpClientBuilder.build();
//
//            // POST 메소드 URL 생성 & header setting
////            HttpClient client = HttpClientBuilder.create().build();
//            HttpPost postRequest = new HttpPost("https://dslabjbnu.iptime.org:8888");
//            postRequest.setHeader("Accept", MediaType.APPLICATION_JSON_VALUE);
//            postRequest.setHeader("Connection", "keep-alive");
//            postRequest.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
//
//            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//            nameValuePairs.add(new BasicNameValuePair("login", "user@example.com"));
//            nameValuePairs.add(new BasicNameValuePair("password", "12341234"));
//            postRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//            // CURL execute
//            HttpResponse response = client.execute(postRequest);
//            System.out.println(response);
//        } catch (Exception e){
//            e.printStackTrace();
//        }

//    }

//    /*
//        이미지 파일을 Base64 로 인코딩
//     */
    private String getBase64String(MultipartFile multipartFile) throws Exception {
        byte[] bytes = multipartFile.getBytes();
        return Base64.getEncoder().encodeToString(bytes);
    }

    /*
        다른 서버 api 호출에 필요한 RestTemplate 객체 생성
     */
    private RestTemplate makeRestTemplate(boolean ignoreSsl)throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        // https 서버 호출
        if(ignoreSsl){
            // http client builder
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

            // 모든 인증서를 신뢰하도록 설정
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (X509Certificate[] chain, String authType) -> true).build();
            httpClientBuilder.setSSLContext(sslContext);

            // Https 인증 요청시 호스트네임 유효성 검사를 진행하지 않게 한다.
            SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslSocketFactory).build();

            PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            httpClientBuilder.setConnectionManager(connMgr); // http client builder 에 설정

            // build 하여 httpclient 생성, RestTemplate 와 HttpClient 연결
            HttpClient httpClient = httpClientBuilder.build();
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(httpClient);

            RestTemplate restTemplate = new RestTemplate(requestFactory);
            return restTemplate;

        }else{ // http 서버 호출
            return new RestTemplate();
        }
    }

    /*
        다른 api 서버에 요청하여 딥페이크 검출한 후 결과 반환
     */
    private boolean deepFake(Cookie cookie, MultipartFile file) throws Exception{

        String url = "https://dslabjbnu.iptime.org:8888"; // API 서버

        RestTemplate restTemplate = makeRestTemplate(true);

        // http 헤더 세팅
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        // 전송할 Body
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        //String imageFileString = getBase64String(file);  // 이미지파일을 json 형태로 보내기 위해 base64로 인코딩
        body.add("login", "user@example.com");
        body.add("password", "12341234");
        //body.add("image", imageFileString);

        // header, body 를 담아 requestMessage 생성
        HttpEntity<?> requestMessage = new HttpEntity<>(body, httpHeaders);

        // API 서버에 요청 보낸 후 응답 받기
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestMessage, String.class);

        // 서버 정상 연결 확인
        System.out.println(response.getHeaders());
        //assert(response.getStatusCode()).equals(HttpStatus.OK);

        // 딥페이크 검출 여부 반환
        //boolean isDeepfake = response.getBody();
        return true;
    }


}
