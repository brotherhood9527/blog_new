package com.example.demo.dao;

import com.example.demo.model.pojo.LikeRecord;

import java.util.List;

public interface LikeRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LikeRecord row);

    int insertSelective(LikeRecord row);

    LikeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LikeRecord row);

    int updateByPrimaryKey(LikeRecord row);

    List<LikeRecord> selectBycontentTypeIdAndobjIDAnduserId(Integer contentTypeId, Integer objId, Integer userId);
}