package com.example.demo.model.param;/*

 */

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ForgotPasswordParam {
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱的格式不正确")
    private String email;

    @NotNull(message = "验证码不能为空")
    private Integer verificationCode;

    @NotBlank(message = "新密码不能为空")
    private String newPassword;

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

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
