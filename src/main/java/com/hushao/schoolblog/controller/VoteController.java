package com.hushao.schoolblog.controller;

import com.hushao.schoolblog.domain.User;
import com.hushao.schoolblog.service.BlogService;
import com.hushao.schoolblog.service.VoteService;
import com.hushao.schoolblog.util.ExceptionHandlerAdvice;
import com.hushao.schoolblog.vo.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;

/**
 * @author hushao
 * @date 2017/10/21
 */
@Controller
@RequestMapping("/votes")
public class VoteController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private VoteService voteService;

    /**
     * 点赞
     * @param blogId
     * @return
     */
    @PostMapping
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('ROLE_ADMINistrator','ROLE_ADMIN','ROLE_USER')")
    public ErrorResponse createVote(@RequestParam("blogId") Long blogId) throws Exception{
        blogService.createVote(blogId);
        return new  ErrorResponse(true,"点赞成功！");
    }

    /**
     * 取消点赞
     * @param voteId
     * @param blogId
     * @return
     */
    @ResponseBody
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMINistrator','ROLE_ADMIN','ROLE_USER')")
    public ErrorResponse delete(@PathVariable("id") Long voteId, @RequestParam("blogId") Long blogId)throws Exception{
        boolean isOwner=false;
        User user=voteService.getVoteById(voteId).getUser();
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
        blogService.removeVote(blogId,voteId);
        voteService.removeVote(voteId);
        return new ErrorResponse(true,"取消点赞成功！");
    }
}
