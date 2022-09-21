package com.example.demo.service;/*

 */

import com.example.demo.dao.UserProfileMapper;
import com.example.demo.model.pojo.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {
    @Autowired
    private UserProfileMapper userProfileMapper;
    public int updateByPrimaryKeySelective(UserProfile row){
        return userProfileMapper.updateByPrimaryKeySelective(row);
    }

    public int insertSelective(UserProfile row){
        return userProfileMapper.insertSelective(row);
    }
}
