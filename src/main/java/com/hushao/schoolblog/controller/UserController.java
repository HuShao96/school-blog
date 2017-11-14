package com.hushao.schoolblog.controller;

import com.hushao.schoolblog.domain.Authority;
import com.hushao.schoolblog.domain.Catalog;
import com.hushao.schoolblog.domain.User;
import com.hushao.schoolblog.service.AuthoritySerivice;
import com.hushao.schoolblog.service.CatalogService;
import com.hushao.schoolblog.service.UserService;
import com.hushao.schoolblog.util.ExceptionHandlerAdvice;
import com.hushao.schoolblog.vo.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户控制器
 * @author 胡少
 */
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthoritySerivice authoritySerivice;
    @Autowired
    private CatalogService catalogService;

    /**
     * 查询所有用户
     * @param async 异步
     * @param pageIndex 页数
     * @param pageSize 一页显示的数据数
     * @param nickname 昵称
     * @param model
     * @return
     */
    @GetMapping
    public ModelAndView list(@RequestParam(value = "async",required = false) boolean async,
                             @RequestParam(value = "pageIndex",required = false,defaultValue = "0") int pageIndex,
                             @RequestParam(value = "pageSize",required = false,defaultValue = "10") int pageSize,
                             @RequestParam(value = "nickname",required = false,defaultValue = "") String nickname,
                             Model model) throws Exception{
        Pageable pageable=new PageRequest(pageIndex,pageSize);
        Page<User> page=userService.listUsersByNameLike(nickname,pageable);
        //当前页数的页面列表
        List<User> list=page.getContent();
        model.addAttribute("page",page);
        model.addAttribute("userList",list);
        return new ModelAndView(async==true?"users/list :: #mainContainerRepleace":"users/list", "userModel", model);
    }

    /**
     * 获取创建表单页面
     * @param model
     * @return
     */
    @GetMapping("/add")
    public ModelAndView createForm(Model model) throws Exception{
        model.addAttribute("user",new User(null,null,null,null));
        return new ModelAndView("users/add","userModel",model);
    }

    /**
     * 保存或者修改用户
     * @param user
     * @return
     */
    @PostMapping
    @ResponseBody
    public ErrorResponse saveOrUpdateUser(User user, Long authorityId) throws Exception{
        List<Authority> authorities=new ArrayList<>();
        //查询权限
        authorities.add(authoritySerivice.getAuthorityById(authorityId));
        //赋值给user.setAuthorities()
        user.setAuthorities(authorities);
        userService.saveOrUpateUser(user);
        return new ErrorResponse(true,"处理成功！");
    }

    /**
     * 删除用户
     * @param userId
     * @return
     */
    @ResponseBody
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMINistrator')")
    public ErrorResponse delete(@PathVariable("userId") Long userId) throws Exception{

        userService.removeUser(userId);
        return new ErrorResponse(true,"删除用户成功！");
    }

    /**
     * 获取修改用户的界面
     * @param id
     * @param model
     * @return
     */
    @GetMapping(value = "edit/{id}")
    public ModelAndView modifyForm(@PathVariable("id") Long id,Model model) throws Exception{
        User user=userService.getUserById(id);
        model.addAttribute("user",user);
        return new ModelAndView("users/edit","userModel",model);
    }

}
