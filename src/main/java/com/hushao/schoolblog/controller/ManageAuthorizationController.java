package com.hushao.schoolblog.controller;

import com.hushao.schoolblog.domain.Catalog;
import com.hushao.schoolblog.domain.User;
import com.hushao.schoolblog.service.CatalogService;
import com.hushao.schoolblog.service.UserService;
import com.hushao.schoolblog.vo.CatalogResponse;
import com.hushao.schoolblog.vo.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 930588706
 * @date 2017/11/7
 */
@Controller
@RequestMapping("/manageAuthorization")
public class ManageAuthorizationController {
    @Autowired
    private UserService userService;
    @Autowired
    private CatalogService catalogService;

    /**
     * 管理员列表
     * @param nickname
     * @param async
     * @param pageIndex
     * @param pageSize
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMINistrator')")
    public String listAdmin( @RequestParam(value = "nickname",required = false,defaultValue = "") String nickname,
                                @RequestParam(value = "async",required = false) boolean async,
                                @RequestParam(value = "pageIndex",required = false,defaultValue = "0")int pageIndex,
                                @RequestParam(value = "pageSize",required = false,defaultValue = "10")int pageSize,
                                Model model) throws Exception{
        Pageable pageable=new PageRequest(pageIndex,pageSize);
        Page<User> page=userService.listAdminsByNameLike(nickname,pageable);
        //当前页数的页面列表
        List<User> list=page.getContent();
        model.addAttribute("page",page);
        model.addAttribute("adminList",list);
        return (async==true?"/manageAuthorizations/adminList ::#mainContainerRepleace":"/manageAuthorizations/adminList");
    }

    /**
     * 获取分类列表
     * @param userId
     * @param catalogName
     * @param async
     * @param pageIndex
     * @param pageSize
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping("/{userId}/catalogs")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMINistrator')")
    public String listCatalogs( @PathVariable("userId") Long userId,
                                @RequestParam(value = "catalogName",required = false,defaultValue = "") String catalogName,
                                @RequestParam(value = "async",required = false) boolean async,
                                @RequestParam(value = "pageIndex",required = false,defaultValue = "0")int pageIndex,
                                @RequestParam(value = "pageSize",required = false,defaultValue = "12")int pageSize,
                                Model model) throws Exception{
        Pageable pageable=new PageRequest(pageIndex,pageSize);
        Page<Catalog> page=catalogService.listCatalogByName(catalogName,pageable);
        List<Catalog> list=page.getContent();
        //获取当前用户拥有哪些分类权限
        List<Catalog> userCatalogAuthority=userService.getUserById(userId).getCatalogs();
        //两集合的交集
        userCatalogAuthority.retainAll(list);

        List<CatalogResponse> catalogResponses=new ArrayList<>();
        for (Catalog c1:list) {
            boolean flag = false;
            for(Catalog c2:userCatalogAuthority){
                if(c1.getId().equals(c2.getId())){
                    catalogResponses.add(new CatalogResponse(c1.getId(),c1.getCatalogName(),"true"));
                    flag=true;
                }
            }
            if(!flag){
                catalogResponses.add(new CatalogResponse(c1.getId(),c1.getCatalogName(),"false"));
            }

        }
        model.addAttribute("page",page);
        model.addAttribute("catalogs",catalogResponses);
        model.addAttribute("userId",userId);
        return (async==true?"/manageAuthorizations/catalogList ::#catalogContainerRepleace":"/manageAuthorizations/catalogList");
    }

    /**
     * 添加授权
     * @param userId
     * @param catalogId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('ROLE_ADMINistrator')")
    @PostMapping("/{userId}")
    public ErrorResponse addAuthority(@PathVariable("userId") Long userId, @RequestParam("catalogId") Long catalogId) throws Exception{
        User user=userService.getUserById(userId);
        Catalog catalog=catalogService.getCatalogById(catalogId);
        List<Catalog> catalogs=user.getCatalogs();
        catalogs.add(catalog);
        user.setCatalogs(catalogs);
        userService.saveOrUpateUser(user);
        return new ErrorResponse(true,"授权成功");
    }

    /**
     * 删除授权
     * @param userId
     * @param catalogId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('ROLE_ADMINistrator')")
    @DeleteMapping("/{userId}")
    public ErrorResponse cancelAuthority(@PathVariable("userId") Long userId,@RequestParam("catalogId") Long catalogId) throws Exception {
        User user=userService.getUserById(userId);
        List<Catalog> catalogs=user.getCatalogs();
        for(int i=0;i<catalogs.size();i++){
            if(catalogs.get(i).getId().equals(catalogId)){
                catalogs.remove(i);
            }
        }
        user.setCatalogs(catalogs);
        userService.saveOrUpateUser(user);
        return new ErrorResponse(true,"取消授权成功");
    }
}
