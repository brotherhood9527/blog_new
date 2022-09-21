package com.example.demo.controller;/*

 */

import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(){ return "/user/login";}

    @GetMapping("/register")
    public String register(){return "/user/register";}

    @RequestMapping("/logout")
    public void logout(@RequestParam String from, HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().removeAttribute("user");
        response.sendRedirect(from);
    }

    @GetMapping("/userInfo")
    public String userInfo(){
        return "/user/user_info";
    }

    @GetMapping("/changeNickName")
    public String changeNickName(){
        return "/user/change_nickname";
    }

    @GetMapping("/bindEmail")
    public String bindEmail(){
        return "/user/bind_email";
    }

    @GetMapping("/changePassword")
    public String changePassword(){return "/user/change_password"; }

    @GetMapping("/forgotPassword")
    public String forgotPassword(){ return"/user/forgot_password"; }
}
