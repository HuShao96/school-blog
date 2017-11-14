package com.hushao.schoolblog.controller;

import com.hushao.schoolblog.vo.Menu;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * 后台管理控制器
 * @author hushao
 */
@Controller
@RequestMapping("/admins")
public class AdminController {
    /**
     * 获取后台管理主页面
     * @param model
     * @return
     */
    @GetMapping
    public ModelAndView listUsers(Model model){
        List<Menu> list=new ArrayList<>();
        list.add(new Menu("用户管理","/users"));
        list.add(new Menu("分类管理","/catalogs"));
        list.add(new Menu("管理授权","/manageAuthorization"));
        model.addAttribute("list",list);
        return new ModelAndView("admins/index","menuList",model);
    }
}
