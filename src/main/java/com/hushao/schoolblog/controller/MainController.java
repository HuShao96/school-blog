package com.hushao.schoolblog.controller;

import com.hushao.schoolblog.domain.Authority;
import com.hushao.schoolblog.domain.User;
import com.hushao.schoolblog.service.AuthoritySerivice;
import com.hushao.schoolblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页控制器
 * @author hushao
 */
@Controller
public class MainController {
    private static final Long ROLE_USER_AUTHORITY_ID = 3L;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthoritySerivice authoritySerivice;
    @GetMapping("/")
    public String root(){
        return "redirect:/blogs";
    }
    @GetMapping("/index")
    public String index(){
        return "redirece:/blogs";
    }
    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @GetMapping("/login-error")
    public String loginError(Model model){
        model.addAttribute("loginError",true);
        model.addAttribute("errorMsg","登陆失败！");
        return "login";
    }
    @GetMapping("/register")
    public String register() {
        return "register";
    }
    @PostMapping("/register")
    public String registerUser(User user) throws Exception{
        List<Authority> authorities=new ArrayList<>();
        authorities.add(authoritySerivice.getAuthorityById(ROLE_USER_AUTHORITY_ID));
        user.setAuthorities(authorities);
        userService.registerUser(user);
        return "redirect:/login";
    }
}
