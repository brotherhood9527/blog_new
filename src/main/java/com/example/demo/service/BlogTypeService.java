package com.example.demo.service;/*

 */

import com.example.demo.dao.BlogTypeMapper;
import com.example.demo.model.pojo.BlogType;
import com.example.demo.model.vo.BlogTypeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogTypeService {
    @Autowired
    private BlogTypeMapper blogTypeMapper;

    public BlogType selectByPrimaryKey(int id){
        return blogTypeMapper.selectByPrimaryKey(id);
    }

    public List<BlogTypeVo> getBlogTypeAndBlogCount(){
        return blogTypeMapper.getBlogTypeAndBlogCount();
    }
}
