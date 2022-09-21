package com.example.demo.controller;/*

 */

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.example.demo.model.param.*;
import com.example.demo.model.pojo.User;
import com.example.demo.model.pojo.UserProfile;
import com.example.demo.model.vo.UserVo;
import com.example.demo.service.MailService;
import com.example.demo.service.UserProfileService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/user")
public class UserFormController {
    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserProfileService userProfileService;

    @RequestMapping("/login")
    public Object login(@RequestParam(defaultValue = "/") String from, @RequestBody @Valid LoginParam loginParam, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(bindingResult.hasErrors()){
            //表单验证异常
        }
        Map<String, Object> login = new HashMap<>();
        HttpSession session = request.getSession();
        try {
            login = userService.login(loginParam.getUserNameOrEmail(), loginParam.getPassword());
            if((Boolean)(login.get("status"))){
                session.setAttribute("user",login.get("user"));
            }
        }catch (Exception e){
            //异常处理
        }
        return JSON.toJSON(login);
    }

    @PostMapping("/loginForModal")
    public Object loginForMoal(@RequestBody @Valid LoginParam loginParam, BindingResult bindingResult, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            //表单验证异常
        }
        HttpSession session = request.getSession();
        Map<String, Object> loginForMoal = new HashMap<>();
        try {
            loginForMoal = userService.login(loginParam.getUserNameOrEmail(), loginParam.getPassword());
            if((Boolean)(loginForMoal.get("status"))){
                session.setAttribute("user",loginForMoal.get("user"));
            }
        }catch (Exception e){
            //异常处理
        }
        return loginForMoal;
    }

    @PostMapping("/register")
    public Object register(@RequestParam(defaultValue = "/") String from, @RequestBody @Valid RegisterParam registerParam, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response){
        if(bindingResult.hasErrors()){
            //表单验证异常
        }
        Map<String, Object> register = new HashMap<>();
        HttpSession session = request.getSession();
        int registerCode = (Integer)session.getAttribute("registerCode");
        if(registerCode == registerParam.getVerificationCode()){
            register = userService.register(registerParam.getUsername(), registerParam.getEmail(), registerParam.getPassword());
            session.removeAttribute("registerCode");
        }else{
            register.put("status",false);
            register.put("message","两次输入的验证码不一致");
            register.put("errorId","verificationCode_error");
        }
        return register;
    }

    @RequestMapping("/sendVerificationCode")
    public Object sendVerificationCode(@RequestParam String email, @RequestParam String sendFor, HttpServletRequest request){
        Map<String, Object> sendVerificationCode = new HashMap<>();
        HttpSession session = request.getSession();
        if(Validator.isEmail(email)){
            sendVerificationCode.put("status",true);
            int code = new Random().nextInt(899999) + 10000;
            long sendForTime = 0;
            Object SessionsendForTime = session.getAttribute("sendForTime");
            if(SessionsendForTime != null){
                sendForTime = (long)SessionsendForTime;
            }
            long currentTimeMillis = System.currentTimeMillis();
            if((currentTimeMillis - sendForTime) < 30){
                sendVerificationCode.put("disable",false);
            }else{
                session.setAttribute(sendFor,code);
                session.setAttribute("sendForTime",currentTimeMillis);
                sendVerificationCode.put("disable",true);
                mailService.sendVerificationCodeMail(code,email);
            }
        }else{
            sendVerificationCode.put("status",false);
            sendVerificationCode.put("message","请输入正确的邮箱格式");
            sendVerificationCode.put("errorId","email_error");
        }
        return sendVerificationCode;
    }

    @PostMapping("/changeNickName")
    public Object changeNickName(@RequestParam(required = false) String nicknameNew,HttpServletRequest request){
        Map<String, Object> changeNickName = new HashMap<>();
        if(!StrUtil.isBlank(nicknameNew)){
            UserVo user = (UserVo) request.getSession().getAttribute("user");
            UserProfile userProfile = user.getUserProfile();
            if(user.getUserProfile() == null){
                userProfile = new UserProfile();
                userProfile.setNickname(nicknameNew);
                userProfile.setUserId(user.getId());
                userProfileService.insertSelective(userProfile);
            }else {
                userProfile.setNickname(nicknameNew);
                userProfileService.updateByPrimaryKeySelective(userProfile);
            }
            user.setUserProfile(userProfile);
            changeNickName.put("status",true);
        }
        return changeNickName;
    }

    @PostMapping("/bindEmail")
    public Object bindEmail(@RequestBody @Valid BindEmailParam bindEmailParam, BindingResult bindingResult, HttpServletRequest request){
        Map<String, Object> bindEmail = new HashMap<>();
        if(bindingResult.hasErrors()){
            //异常处理
        }
        HttpSession session = request.getSession();
        UserVo user = (UserVo) session.getAttribute("user");
        String verificationCode = String.valueOf(bindEmailParam.getVerificationCode());

        if(user != null &&  verificationCode.equals(session.getAttribute("bind_email_code") + "")){
            user.setEmail(bindEmailParam.getEmail());
            bindEmail = userService.bindEmail(user.getId(),bindEmailParam.getEmail(),bindEmailParam.getVerificationCode());
            request.getSession().removeAttribute("bind_email_code");
        }else{
            bindEmail.put("status",false);
            bindEmail.put("message","验证码不正确");
            bindEmail.put("errorId","verificationCode_error");
        }
        return bindEmail;
    }

    @PostMapping("/changePassword")
    public Object changePassword(@RequestBody @Valid ChangePasswordParam changePasswordParam, BindingResult bindingResult, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            //异常处理
        }
        Map<String, Object> changePassword = new HashMap<>();
        HttpSession session = request.getSession();
        UserVo userVo = (UserVo)session.getAttribute("user");
        if(changePasswordParam.getNewPassword().equals(changePasswordParam.getNewPasswordAgain())){
            Map<String, Object> login = userService.login(userVo.getUsername(), changePasswordParam.getOldPassword());
            if((Boolean)login.get("status") == true){
                User user = userService.selectByPrimaryKey(userVo.getId());
                user.setPassword(SecureUtil.md5(changePasswordParam.getNewPassword()));
                userService.updateByPrimaryKeySelective(user);
            }else{
                changePassword.put("message",login.get("message"));
                changePassword.put("errorId","tip");
            }
            changePassword.put("status", login.get("status"));
        }else{
            changePassword.put("status",false);
            changePassword.put("message","两次输入的密码不一致");
            changePassword.put("errorId","tip");
        }
        return  changePassword;
    }

    @PostMapping("/forgotPassword")
    public Object forgotPassword(@RequestBody @Valid ForgotPasswordParam forgotPasswordParam, BindingResult  bindingResult, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            //异常处理
        }
        Map<String, Object> forgotPassword = new HashMap<>();
        HttpSession session = request.getSession();
        String verificationCode = String.valueOf(forgotPasswordParam.getVerificationCode());
        if(verificationCode.equals(session.getAttribute("forgot_password_code") + "")){
            forgotPassword = userService.forgotPassword(forgotPasswordParam.getEmail(), forgotPasswordParam.getNewPassword());
            session.removeAttribute("forgot_password_code");
        }else{
            forgotPassword.put("status",false);
            forgotPassword.put("message","验证码不正确");
            forgotPassword.put("errorId","verificationCode_error");
        }
        return forgotPassword;
    }
}
