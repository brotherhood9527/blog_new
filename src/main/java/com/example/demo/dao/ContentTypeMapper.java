package com.example.demo.dao;

import com.example.demo.model.pojo.ContentType;

public interface ContentTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ContentType row);

    int insertSelective(ContentType row);

    ContentType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ContentType row);

    int updateByPrimaryKey(ContentType row);
}