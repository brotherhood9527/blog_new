package com.example.demo.model.param;/*

 */

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RegisterParam {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 30, message = "用户名长度必须在 {min} - {max} 之间")
    private String username;

    @NotBlank(message = "电子邮件的地址不能为空")
    @Email(message = "电子邮件的地址不正确")
    private String email;

    @NotNull(message = "验证码不能为空")
    private Integer verificationCode;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "再次输入的密码不能为空")
    private String passwordAgain;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordAgain() {
        return passwordAgain;
    }

    public void setPasswordAgain(String passwordAgain) {
        this.passwordAgain = passwordAgain;
    }
}
