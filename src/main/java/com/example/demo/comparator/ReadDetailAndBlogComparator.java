package com.example.demo.comparator;

import com.example.demo.model.vo.ReadDetailAndBlog;

import java.util.Comparator;

public class ReadDetailAndBlogComparator implements Comparator<ReadDetailAndBlog> {
    @Override
    public int compare(ReadDetailAndBlog o1, ReadDetailAndBlog o2) {
        return o2.getReadNum() - o1.getReadNum();
    }
}
