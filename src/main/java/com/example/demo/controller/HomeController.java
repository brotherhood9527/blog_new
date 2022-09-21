package com.example.demo.controller;/*

 */

import com.example.demo.utils.ReadStatistics;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HomeController {

    @RequestMapping("")
    public String home(Model model){
        List<List> sevenDaysReadData = ReadStatistics.getSevenDaysReadData();
        model.addAttribute("hotBlogsForSevenDays",ReadStatistics.getSevenDaysBlog());
        model.addAttribute("dates",sevenDaysReadData.get(0));
        model.addAttribute("readNumSum",sevenDaysReadData.get(1));
        model.addAttribute("todayHotData",ReadStatistics.getTodayHotData());
        model.addAttribute("yesterdayHotData",ReadStatistics.getYesterdayHotData());
        return "/home";
    }
}
