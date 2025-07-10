package com.example.recaptcha.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecaptchaResponse {    //보안문자에 사용할 DTO
    private boolean success;
    private String challengeTs;
    private String hostname;
    private String errorCodes;

    public boolean isSuccess() {
        return success;
    }
}