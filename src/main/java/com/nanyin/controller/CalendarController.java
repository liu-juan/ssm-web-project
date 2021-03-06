package com.nanyin.controller;

import com.nanyin.entity.Calendar;
import com.nanyin.entity.user.User;
import com.nanyin.services.CalendarService;
import com.nanyin.services.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by NanYin on 2017-08-15 下午10:40.
 * 包名： com.nanyin.controller
 * 类描述：日程表的controller类
 */
@Controller
@RequestMapping("/cal")
public class CalendarController {
    Logger logger = LogManager.getLogger(this.getClass());
    @Autowired
    CalendarService calendarService;
    @Autowired
    UserService userService;

    @RequestMapping("/returnCal")
    public String retrunCal(){
        return "/static/fondPage/monitor/calendar.jsp";
    }

    @RequestMapping("/can")
    public String can(){
        return "/static/fondPage/monitor/can.jsp";
    }


    @RequestMapping("/allEvent")
    public @ResponseBody List<Calendar> findEventByUserName(HttpServletRequest request ){
        HttpSession name4 = request.getSession();
        String username = (String) name4.getAttribute("user");

        return calendarService.findEventByUserName(username);
    }

    @RequestMapping(value = "/insertEvent"
            ,consumes = "application/json" , method = RequestMethod.POST)
    public @ResponseBody int insertEventByUserName(@RequestBody List<Calendar> calendar, HttpServletRequest request){
        HttpSession session = request.getSession();
        String username = ( String ) session.getAttribute("user");
        User user = userService.findUserByName(username);
        int id = user.getId();
        calendar.get(0).setU_id(id);
        return calendarService.insertEventByUserName(calendar.get(0));
    }
    @RequestMapping(value = "/updateEvent"
            ,consumes = "application/json" , method = RequestMethod.POST)
    public @ResponseBody int updateEvent(@RequestBody List<Calendar> up){
        logger.info(up.get(0).getClassName());
        if(up.get(0).getClassName()==1){
//      这里把颜色固定到程序里
            up.get(0).setColor("#FFEBAC");
        }
        logger.info(up.get(0).toString());
        calendarService.updateEvent(up.get(0));
        return 0 ;
    }

    @RequestMapping(value = "/delectEvent"
            ,consumes = "application/json" , method = RequestMethod.POST)
    public @ResponseBody int delectEvent(@RequestBody Calendar calendar){
        logger.info(calendar);
        int HiddenId = calendar.getId();

        return calendarService.delectEvent(HiddenId);
    }
    @RequestMapping(value = "/count")
    public @ResponseBody
    Map<String,Integer> selectUnfinshEvents(HttpServletRequest request){
        HttpSession session = request.getSession();
        String user = (String) session.getAttribute("user");
        int count = calendarService.selectUnfinshEvents(user);
        Map<String,Integer> map = new HashMap<String, Integer>();
        map.put("count",count);
        return map;
    }
}
