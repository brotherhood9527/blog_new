package com.example.demo.model.param;/*

 */

import javax.validation.constraints.NotBlank;

public class LoginParam {

    @NotBlank(message = "用户名或密码不能为空")
    private String userNameOrEmail;

    @NotBlank(message = "密码不能为空")
    private String password;

    public String getUserNameOrEmail() {
        return userNameOrEmail;
    }

    public void setUserNameOrEmail(String userNameOrEmail) {
        this.userNameOrEmail = userNameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
