package com.nanyin.controller;

import com.alibaba.fastjson.JSON;
import com.nanyin.entity.navBar.NavBarCategory;
import com.nanyin.entity.navBar.vo.NavBarVo;
import com.nanyin.entity.user.User;
import com.nanyin.services.NavBarService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: NanYin
 * @Date: 12/2/18 17:36
 * @Description:
 */
@Controller
@RequestMapping("/nav")
public class NavbarController {

    @Autowired
    NavBarService navBarService;

    @RequestMapping(value = "/navBar", method = RequestMethod.GET)
    public @ResponseBody List<NavBarVo> findNavBarByUserId(Integer category) {
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        return navBarService.findNavTree(user.getId(), category);
    }

    @RequestMapping(value = "/navbarCategory", method = RequestMethod.GET)
    public @ResponseBody List<NavBarCategory> findCategoryByUserId() {
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        return navBarService.findCategoryByUserId(user.getId());
    }

    @RequestMapping(value = "/navbarCategoryTable", method = RequestMethod.GET)
    public @ResponseBody
    Map<String,Object>  navbarCategoryTable() {
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code","");
        try{
            map.put("count",navBarService.findCategoryByUserId(user.getId()).size());
            map.put("data",navBarService.findCategoryByUserId(user.getId()));
            map.put("msg","");
        }catch (Exception e){
            map.put("msg","加载onelevel nav时出现问题");
        }
        return map;
    }

    @RequestMapping(value = "/navManage",method = RequestMethod.GET)
    public String navManage(){
        return "/WEB-INF/jsp/admin/nav/navManage.jsp";
    }

    /*删除一级菜单 todo*/
    @RequestMapping(value = "/oneLevelBar/{id}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    boolean deleteOneLevelNavBar(@PathVariable(value = "id") Integer id) {
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        try {
            navBarService.deleteOneLevelBarByUserId(user.getId(), id);
            if (!navBarService.checkOneLevelBarIsUsed(id)) {
                navBarService.deleteNavCategoryById(id);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*更新或者添加数据 todo*/
    @RequestMapping(value = "/oneLevelBar/{id}", method = RequestMethod.POST)
    public
    @ResponseBody
    boolean addOrUpdateOneLevelNavBar(NavBarCategory category, @PathVariable(value = "id") Integer id) {
        return true;
    }

    @RequestMapping(value = "/addNavCategoryPage",method = RequestMethod.GET)
    public String addNavCategoryPage(){
        return "/WEB-INF/jsp/admin/nav/addNavCategory.jsp";
    }


}