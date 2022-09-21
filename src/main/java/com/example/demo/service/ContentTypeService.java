package com.example.demo.service;/*

 */

import com.example.demo.dao.ContentTypeMapper;
import com.example.demo.model.pojo.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContentTypeService {
    @Autowired
    private ContentTypeMapper contentTypeMapper;

    public ContentType selectByPrimaryKey(Integer id){
        return contentTypeMapper.selectByPrimaryKey(id);
    }
}
