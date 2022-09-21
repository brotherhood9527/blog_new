package com.example.demo.utils;/*

 */

import com.example.demo.comparator.ReadDetailAndBlogComparator;
import com.example.demo.comparator.ReadNumSumComparator;
import com.example.demo.model.pojo.ContentType;
import com.example.demo.model.vo.BlogAndReadNumSum;
import com.example.demo.model.vo.BlogVo;
import com.example.demo.model.vo.ReadDetailAndBlog;
import com.example.demo.service.BlogService;
import com.example.demo.service.ContentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ReadStatistics {
    private static JedisPool jedisPool;

    private static BlogService blogService;

    private static ContentTypeService contentTypeService;

    public static BlogService getBlogService() {
        return blogService;
    }

    public static JedisPool getJedisPool() {
        return jedisPool;
    }

    @Autowired
    public void setJedisPool(JedisPool jedisPool) {
        ReadStatistics.jedisPool = jedisPool;
    }

    @Autowired
    public void setBlogService(BlogService blogService) {
        ReadStatistics.blogService = blogService;
    }

    public ContentTypeService getContentTypeService() {
        return contentTypeService;
    }

    @Autowired
    public void setContentTypeService(ContentTypeService contentTypeService) {
        ReadStatistics.contentTypeService = contentTypeService;
    }

    public static String readStatisticsOnceRead(HttpServletRequest request, int contentTypeId, int objId){
        ContentType contentType = contentTypeService.selectByPrimaryKey(contentTypeId);
        String cookie = String.format("%s_%s_read", contentType.getModel(), objId);
        Cookie[] cookies = request.getCookies();
        ArrayList nameCookies = new ArrayList<String>();
        if(cookies != null){
            for(Cookie cookie1 : cookies) {
                nameCookies.add(cookie1.getName());
            }
        }
        if(cookies == null || !nameCookies.contains(cookie)){
            Jedis jedis = new Jedis();
            try {
                //jedis + string contentTypeID and objID
                jedis = jedisPool.getResource();
                String stringContentTypeId = String.valueOf(contentTypeId);
                String stringObjID = String.valueOf(objId);
                //currnt time
                SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
                sdf.applyPattern("yyyy-MM-dd");// a为am/pm的标记
                Date date = new Date(System.currentTimeMillis());// 获取当前时间
                String currentTime = sdf.format(date);
                //stringjoiner value
                String s = null;
                //dates_readNumsum + 1          --getSevenDaysReadData
                StringJoiner dateReadNUmKey = new StringJoiner("_");
                dateReadNUmKey.add(currentTime);
                dateReadNUmKey.add("readNumSum");
                s = dateReadNUmKey.toString();
                if(jedis.exists(s)){
                    jedis.incrBy(s,1);
                }else{
                    jedis.setex(s,60*60*24*7,"1");
                }
                //seven day - every blog readNum  -- todayhotdata yesterdayhotdata
                StringJoiner dateBLogReadNumKey = new StringJoiner("_");
                dateBLogReadNumKey.add(currentTime);
                dateBLogReadNumKey.add("readNum");
                s = dateBLogReadNumKey.toString();
                if(jedis.exists(s)){
                    jedis.zincrby(s,1,stringObjID);
                }else{
                    jedis.zadd(s,1,stringObjID);
                    jedis.expire(s,(long)60*60*24*7);
                }
                //every blog总阅读数+1  --getblogreadNum
                StringJoiner readNUmKey = new StringJoiner("_");
                readNUmKey.add("readNum");
                readNUmKey.add(stringContentTypeId);
                readNUmKey.add(stringObjID);
                s = readNUmKey.toString();
                if(jedis.exists(s)){
                    jedis.hincrBy(s,"read_num",1);
                }else{
                    jedis.hsetnx(s,"read_num","1");
                }

            }catch (Exception e){
                e.printStackTrace();
            }finally {
                jedis.close();
            }
        }
        return cookie;
    }

    public static List<List> getSevenDaysReadData(){
        ArrayList<List> sevenDaysReadData = new ArrayList<>();
        List dates = new ArrayList<>();
        List readNumSUm = new ArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd");// a为am/pm的标记
        Jedis jedis = new Jedis();
        for(int i = -6; i <= 0; i++){
            try{
                jedis = jedisPool.getResource();
                StringJoiner dateReadNUmSumKey = new StringJoiner("_");
                Date date = DateUtils.getBeforeOrAfterDate(new Date(System.currentTimeMillis()), i);
                String time = sdf.format(date);
                dateReadNUmSumKey.add(time);
                dateReadNUmSumKey.add("readNumSum");
                String s = dateReadNUmSumKey.toString();
                if(jedis.exists(s)){
                    readNumSUm.add(jedis.get(s));
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMdd");
                dates.add(simpleDateFormat.format(date));
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                jedis.close();
            }
        }
        sevenDaysReadData.add(dates);
        sevenDaysReadData.add(readNumSUm);
        return sevenDaysReadData;
    }

    public static List<ReadDetailAndBlog> getTodayHotData(){
        List<ReadDetailAndBlog> readDetailAndBlogs = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd");// a为am/pm的标记z
        Date date = new Date(System.currentTimeMillis());// 获取当前时间
        String currentTime = sdf.format(date);
        StringJoiner dateBLogReadNumKey = new StringJoiner("_");
        dateBLogReadNumKey.add(currentTime);
        dateBLogReadNumKey.add("readNum");
        Jedis jedis = new Jedis();
        try{
            jedis = jedisPool.getResource();
            String s = dateBLogReadNumKey.toString();
            if(jedis.exists(s)){
                Set<Tuple> tuples = jedis.zrevrangeWithScores(s, 0, 6);
                for(Tuple tuple: tuples){
                    ReadDetailAndBlog readDetailAndBlog = new ReadDetailAndBlog();
                    Integer blogID = Integer.valueOf(tuple.getElement());
                    readDetailAndBlog.setBlogId(blogID);
                    readDetailAndBlog.setTitle(blogService.getBlogById(blogID).getTitle());
                    readDetailAndBlog.setReadNum((int)tuple.getScore());
                    readDetailAndBlogs.add(readDetailAndBlog);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jedis.close();
        }
        readDetailAndBlogs.sort(new ReadDetailAndBlogComparator());
        return readDetailAndBlogs;
    }

    public static List<ReadDetailAndBlog> getYesterdayHotData(){
        List<ReadDetailAndBlog> readDetailAndBlogs = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd");// a为am/pm的标记z
        Date date = new Date(System.currentTimeMillis());// 获取当前时间
        String yesterDayTIme = sdf.format(DateUtils.getBeforeOrAfterDate(date,-1));
        StringJoiner dateBLogReadNumKey = new StringJoiner("_");
        dateBLogReadNumKey.add(yesterDayTIme);
        dateBLogReadNumKey.add("readNum");
        Jedis jedis = new Jedis();
        try{
            jedis = jedisPool.getResource();
            String s = dateBLogReadNumKey.toString();
            if(jedis.exists(s)){
                Set<Tuple> tuples = jedis.zrevrangeWithScores(s, 0, 6);
                for(Tuple tuple: tuples){
                    ReadDetailAndBlog readDetailAndBlog = new ReadDetailAndBlog();
                    Integer blogID = Integer.valueOf(tuple.getElement());
                    readDetailAndBlog.setBlogId(blogID);
                    readDetailAndBlog.setTitle(blogService.getBlogById(blogID).getTitle());
                    readDetailAndBlog.setReadNum((int)tuple.getScore());
                    readDetailAndBlogs.add(readDetailAndBlog);
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jedis.close();
        }
        readDetailAndBlogs.sort(new ReadDetailAndBlogComparator());
        return readDetailAndBlogs;
    }

    public static List<BlogAndReadNumSum> getSevenDaysBlog(){
        List<BlogAndReadNumSum> blogAndReadNumSums = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd");// a为am/pm的标记z
        Jedis jedis = new Jedis();
        List<BlogVo> allBlogs = blogService.getAllBlogs();
        for(BlogVo blogVo : allBlogs){
            BlogAndReadNumSum blogAndReadNumSum = new BlogAndReadNumSum();
            for(int i = -6; i <= 0 ; i++){
                Date date = new Date(System.currentTimeMillis());// 获取当前时间
                String time = sdf.format(DateUtils.getBeforeOrAfterDate(date,i));
                StringJoiner dateBLogReadNumKey = new StringJoiner("_");
                dateBLogReadNumKey.add(time);
                dateBLogReadNumKey.add("readNum");
                try{
                    jedis = jedisPool.getResource();
                    String s = dateBLogReadNumKey.toString();
                    if(jedis.exists(s)){
                        blogAndReadNumSum.setBlogId(blogVo.getId());
                        blogAndReadNumSum.setTitle(blogVo.getTitle());
                        Set<Tuple> tuples = jedis.zrevrangeWithScores(s, 0, 6);
                        for(Tuple tuple: tuples){
                            if(Integer.valueOf(tuple.getElement()) == blogVo.getId()){
                                int readNumSum = blogAndReadNumSum.getReadNumSum() == null ? 0 : blogAndReadNumSum.getReadNumSum();
                                blogAndReadNumSum.setReadNumSum(readNumSum + (int)tuple.getScore());
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    jedis.close();
                }
            }
            if(blogAndReadNumSum.getReadNumSum() != null){
                blogAndReadNumSums.add(blogAndReadNumSum);
            }
        }
        blogAndReadNumSums.sort(new ReadNumSumComparator());
        return blogAndReadNumSums;
    }

    public static Integer getReadNum(int contentTypeId, int objId){
        StringJoiner readNUmKey = new StringJoiner("_");
        readNUmKey.add("readNum");
        readNUmKey.add(String.valueOf(contentTypeId));
        readNUmKey.add(String.valueOf(objId));
        Jedis jedis = null;
        String readNum = null;
        try {
            jedis = jedisPool.getResource();
            String s = readNUmKey.toString();
            if(jedis.exists(s)){
                readNum = jedis.hget(s, "read_num");
            }else {
                readNum = "0";
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jedis.close();
        }
        return Integer.valueOf(readNum);
    }
}
