package com.example.demo.comparator;

import com.example.demo.model.vo.BlogAndReadNumSum;

import java.util.Comparator;

public class ReadNumSumComparator implements Comparator<BlogAndReadNumSum> {
    @Override
    public int compare(BlogAndReadNumSum o1, BlogAndReadNumSum o2) {
        return o2.getReadNumSum() - o1.getReadNumSum();
    }
}
