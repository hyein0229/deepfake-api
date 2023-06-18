package Deepfake.deepfakeapi.controller;

import Deepfake.deepfakeapi.domain.FileEntity;
import Deepfake.deepfakeapi.service.FileService;
import Deepfake.deepfakeapi.utility.MD5Generator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.el.util.ExceptionUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.jcodec.api.FrameGrab;
import org.jcodec.common.io.NIOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final VideoController videoController;

    /*
        파일 업로드
     */
    @PostMapping("/uploadVideo")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile files) throws Exception{

        // 파일이 존재하면
        if(!files.isEmpty()){
            try {

                // 기존 파일이름을 다른 파일 이름으로 저장하기 위해 변환
                String origFileName = files.getOriginalFilename();
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

                String filePath = savedPath + "\\" + filename + ".mp4";
                // 파일이 로컬에 저장됨
                files.transferTo(new File(filePath));

                /*
                동영상의 경우 이미지 추출
                */
                videoController.videoFrame(filePath, origFileName);

                // DB에 저장될 파일 정보 객체 생성
                FileEntity file = new FileEntity();
                file.setOrigFilename(origFileName);
                file.setStoredFilename(filename);
                file.setFilePath(filePath);

                fileService.saveFile(file);

                return new ResponseEntity<>("Success", HttpStatus.OK);

            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>("File not exists", HttpStatus.OK);
    }

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
            딥페이크 검출
         */
        String result = deepFake(imgFileArray);
        System.out.println(result);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }
    /*
        OK 버튼 클릭하여 이미지 삽입 시 서버로 업로드되어 저장 ( 글 등록과 다름 )
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

    public String deepFake(ArrayList<File> imgFileArray) throws Exception{

        String HOST = "http://dslabjbnu.iptime.org:8082/predictions/efficient"; // API 서버

        RestTemplate restTemplate = makeRestTemplate(true);

        // http 헤더 세팅
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        // 전송할 Body
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        // 전송할 이미지 데이터
        Map<String, List<String>> instance = new HashMap<String, List<String>>();
        List<String> imageArr = new ArrayList<>();
        for(File file : imgFileArray){
            imageArr.add(getBase64String(file)); //이미지파일을 json 형태로 보내기 위해 base64로 인코딩
        }
        instance.put("body", imageArr); // 전송할 이미지 base64 배열
        body.add("instances", instance);

        // header, body 를 담아 requestMessage 생성
        HttpEntity<?> requestMessage = new HttpEntity<>(body, httpHeaders);

        // API 서버에 요청 보낸 후 응답 받기
        ResponseEntity<String> response = restTemplate.postForEntity(HOST, requestMessage, String.class);

        return response.getBody();
    }

    /*
        이미지 파일을 Base64 로 인코딩
     */
    private String getBase64String(File file) throws Exception {

        byte[] bytes = Files.readAllBytes(file.toPath());
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


    public void apiTest2() throws Exception{

        String[] command = new String[2];
        command[0] = "python";
        command[1] = "C:/Users/HAYOUNG LEE/Desktop/deepfake/test3.py";
        try {
            execPython(command);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void execPython(String[] command) throws IOException, InterruptedException {
        CommandLine commandLine = CommandLine.parse(command[0]);
        for (int i = 1, n = command.length; i < n; i++) {
            commandLine.addArgument(command[i]);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(outputStream);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setStreamHandler(pumpStreamHandler);
        int result = executor.execute(commandLine);
        System.out.println("result: " + result);
        String output = outputStream.toString();
        for(int i=0; i < output.length(); i++) {
            if (output.charAt(i) == '1') {
                System.out.println("output: " + 1);
            }
        }
    }
}
