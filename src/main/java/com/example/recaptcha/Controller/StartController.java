package com.example.recaptcha.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.example.recaptcha.DTO.MyFormDTO;
import com.example.recaptcha.DTO.RecaptchaResponse;

@Controller
public class StartController {
    
    @Value("${recaptcha.secret-key}")
    private String recaptchaSecretKey;

    // 입력 페이지로 이동
    @GetMapping("/")
    public String startForm(Model model) {
        model.addAttribute(new MyFormDTO());
        return "form";
    }

    // 입력페이지에서 submit으로 전달했을 때 처리
    @PostMapping("/submitForm")
    public String submitForm(MyFormDTO myFormDTO, // 폼에서 입력한 값들을 받는 DTO
                             @RequestParam("g-recaptcha-response") String recaptchaResponse, // reCAPTCHA 응답 값
                             Model model) { 
        boolean isRecaptchaValid = verifyRecaptcha(recaptchaResponse); 
        if (!isRecaptchaValid) {
            model.addAttribute("error", "Recaptcha verification failed. Please try again.");
            model.addAttribute("myFormDTO", myFormDTO);
            return "form";
        }
        return "success";
    }

    private boolean verifyRecaptcha(String userResponse) {
        // reCAPTCHA 검증을 위한 URL 생성
        String url = "https://www.google.com/recaptcha/api/siteverify?secret=" + recaptchaSecretKey + "&response=" + userResponse;

        // RestTemplate을 사용하여 reCAPTCHA 응답을 검증
        // RestTemplate은 Spring에서 HTTP 요청을 보내고 응답을 받기 위한 클래스
        RestTemplate restTemplate = new RestTemplate();
        // postForObject 메서드를 사용하여 POST 요청을 보내고 RecaptchaResponse 객체로 응답을 받음
        // URL에 쿼리 파라미터로 비밀 키와 사용자 응답을 포함시켜 요청
        // RecaptchaResponse는 reCAPTCHA 응답을 매핑하기 위한 DTO
        RecaptchaResponse recaptchaResponse = restTemplate.postForObject(url, null, RecaptchaResponse.class);
        // reCAPTCHA 응답이 null이 아니고 성공 여부가 true인 경우에만 유효한 것으로 간주
        // isSuccess() 메서드를 사용하여 성공 여부를 확인
        return recaptchaResponse != null && recaptchaResponse.isSuccess();
    }
}