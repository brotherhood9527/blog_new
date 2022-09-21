package com.example.demo.model.vo;/*

 */

public class BlogTypeVo {
    private Integer id;

    private String typeName;

    private int blogCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName == null ? null : typeName.trim();
    }

    public int getBlogCount() { return blogCount; }

    public void setBlogCount(int blogCount) { this.blogCount = blogCount;}
}
