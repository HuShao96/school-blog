package com.hushao.schoolblog.controller;

import com.hushao.schoolblog.domain.User;
import com.hushao.schoolblog.service.AttentionService;
import com.hushao.schoolblog.service.CatalogService;
import com.hushao.schoolblog.vo.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author hushao
 * @date 2017/10/26
 */
@Controller
@RequestMapping("/attentions")
public class AttentionController {
    @Autowired
    private CatalogService catalogService;
    @Autowired
    private AttentionService attentionService;

    /**
     * 关注
     * @param catalogId
     * @return
     */
    @PostMapping
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('ROLE_ADMINistrator','ROLE_ADMIN','ROLE_USER')")
    public ErrorResponse createAttention(@RequestParam("catalogId") Long catalogId) throws Exception{
        catalogService.createAttention(catalogId);
        String url="/catalogs/"+catalogId+"/blogs";
        return new  ErrorResponse(true,"关注成功！",url);
    }
    /**
     * 取消关注
     * @param attentionId
     * @param catalogId
     * @return
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('ROLE_ADMINistrator','ROLE_ADMIN','ROLE_USER')")
    public ErrorResponse removeAttention(@PathVariable("id") Long attentionId, @RequestParam("catalogId") Long catalogId)throws Exception{
        boolean isOwner=false;
        User user=attentionService.getByAttentionId(attentionId).getUser();
        //判断操作用户是否是点赞的所有者
        if(SecurityContextHolder.getContext().getAuthentication()!=null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
            User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(principal!=null&&user.getUsername().equals(principal.getUsername())){
                isOwner=true;
            }
        }
        if(!isOwner){
            throw  new Exception("没有权限!");
        }
        catalogService.removeAttention(catalogId,attentionId);
        attentionService.removeAttention(attentionId);
        String url="/catalogs/"+catalogId+"/blogs";
        return new ErrorResponse(true,"取消关注成功！",url);
    }
}
