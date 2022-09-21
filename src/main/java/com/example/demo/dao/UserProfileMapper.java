package com.example.demo.dao;

import com.example.demo.model.pojo.UserProfile;

public interface UserProfileMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserProfile row);

    int insertSelective(UserProfile row);

    UserProfile selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserProfile row);

    int updateByPrimaryKey(UserProfile row);

    UserProfile selectByUserId(Integer userId);
}