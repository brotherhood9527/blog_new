package com.example.demo.model.param;/*

 */

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class BindEmailParam {
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱的格式不正确")
    private String email;

    @NotNull(message = "验证码不能为空")
    private Integer verificationCode;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(Integer verificationCode) {
        this.verificationCode = verificationCode;
    }
}
