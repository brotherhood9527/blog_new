package com.example.demo.dao;

import com.example.demo.model.pojo.LikeCount;

import java.util.List;

public interface LikeCountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LikeCount row);

    int insertSelective(LikeCount row);

    LikeCount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LikeCount row);

    int updateByPrimaryKey(LikeCount row);

    List<LikeCount> selectBycontentTypeIdAndobjIDAnduserId(Integer contentTypeId, Integer objId);
}