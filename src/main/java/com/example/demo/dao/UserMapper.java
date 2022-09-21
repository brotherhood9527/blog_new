package com.example.demo.dao;

import com.example.demo.model.pojo.User;
import com.example.demo.model.vo.UserVo;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User row);

    int insertSelective(User row);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User row);

    int updateByPrimaryKey(User row);

    List<User> selectUserByUserNameOrEmail(String userNameOrEmail);

    UserVo selectUserWithProfileByUserId(Integer id);

}