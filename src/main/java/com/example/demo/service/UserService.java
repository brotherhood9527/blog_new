package com.example.demo.service;/*

 */

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.example.demo.dao.UserMapper;
import com.example.demo.model.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public Map<String, Object> login(String userNameOrEmail, String password){
        if (StrUtil.isBlank(userNameOrEmail) || StrUtil.isBlank(password)) {
            throw  new RuntimeException("用户名和密码不能为空");
        }
        Map<String, Object> resultMap = new HashMap<>();
        //根据用户名或邮箱进行查询该用户是否存在
        List<User> users = userMapper.selectUserByUserNameOrEmail(userNameOrEmail);
        if(users.isEmpty()){
            resultMap.put("status",false);
            resultMap.put("message","用户名不存在，请重新输入");
            resultMap.put("errorId","userNameOrEmail_error");
            return resultMap;
        }
        User user = users.get(0);
        //密码正确
        if(user.getPassword().equals(SecureUtil.md5(password))){
            user.setLastLogin(new Date(System.currentTimeMillis()));
            userMapper.updateByPrimaryKeySelective(user);
            resultMap.put("status",true);
            resultMap.put("user",userMapper.selectUserWithProfileByUserId(user.getId()));
        }else{
            resultMap.put("status",false);
            resultMap.put("message","密码错误，请重新输入密码");
            resultMap.put("errorId","password_error");
        }
        return resultMap;
    }

    public Map<String, Object> register(String userName, String email, String password){
        Map<String, Object> resultMap = new HashMap<>();
        //查找userName或email邮箱是否被注册
        List<User> userByUserName = userMapper.selectUserByUserNameOrEmail(userName);
        List<User> userByEmail = userMapper.selectUserByUserNameOrEmail(email);
        if(userByUserName.isEmpty()){
            if(userByEmail.isEmpty()){
                resultMap.put("status",true);
                User user = new User();
                user.setUsername(userName);
                user.setEmail(email);
                user.setPassword(SecureUtil.md5(password));
                user.setIsActive(true);
                user.setIsStaff(false);
                user.setIsSuperuser(false);
                user.setFirstName("");
                user.setLastName("");
                user.setDateJoined(new Date(System.currentTimeMillis()));
                userMapper.insertSelective(user);
            }else{
                resultMap.put("status",false);
                resultMap.put("message","邮箱已经被注册");
                resultMap.put("errorId","email_error");
            }
        }else{
            resultMap.put("status",false);
            resultMap.put("message","用户名已经被注册");
            resultMap.put("errorId","username_error");
        }
        return resultMap;
    }

    public Map<String, Object> bindEmail(Integer id, String email, Integer verificationCode){
        Map<String, Object> bindEmail = new HashMap<>();
        if(this.userMapper.selectUserByUserNameOrEmail(email).isEmpty()){
            User user = userMapper.selectByPrimaryKey(id);
            user.setEmail(email);
            userMapper.updateByPrimaryKeySelective(user);
            bindEmail.put("status",true);
        }else{
            bindEmail.put("status",false);
            bindEmail.put("message","邮箱已经被注册");
            bindEmail.put("errorId","email_error");
        }
        return bindEmail;
    }

    public Map<String, Object> forgotPassword(String email,String newPassword){
        Map<String, Object> forgotPassword = new HashMap<>();
        List<User> userByUserNameOrEmail = userMapper.selectUserByUserNameOrEmail(email);
        if(userByUserNameOrEmail.isEmpty()){
            forgotPassword.put("status",false);
            forgotPassword.put("message","邮箱不存在");
            forgotPassword.put("errorId","email_error");
        }else{
            User user = userByUserNameOrEmail.get(0);
            user.setPassword(SecureUtil.md5(newPassword));
            userMapper.updateByPrimaryKeySelective(user);
            forgotPassword.put("status",true);
        }
        return forgotPassword;
    }

    public int updateByPrimaryKeySelective(User row){
        return userMapper.updateByPrimaryKeySelective(row);
    }

    public User selectByPrimaryKey(Integer id){
        return userMapper.selectByPrimaryKey(id);
    }

}
