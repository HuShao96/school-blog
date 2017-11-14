package com.hushao.schoolblog.controller;

import com.hushao.schoolblog.domain.Blog;
import com.hushao.schoolblog.domain.Comment;
import com.hushao.schoolblog.domain.User;
import com.hushao.schoolblog.service.BlogService;
import com.hushao.schoolblog.service.CommentService;
import com.hushao.schoolblog.util.AuthorityUtil;
import com.hushao.schoolblog.util.ExceptionHandlerAdvice;
import com.hushao.schoolblog.vo.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * @author hushao
 * @date 2017/10/20
 */
@Controller
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private AuthorityUtil authorityUtil;
    /**
     * 获取评论列表
     * @param blogId
     * @param catalogId
     * @param model
     * @return
     */
    @GetMapping
    public String listComments(@RequestParam(value = "blogId") Long blogId,@RequestParam(value = "catalogId")Long catalogId ,Model model) throws Exception{
        Blog blog=blogService.getBlogById(blogId);
        List<Comment> comments=blog.getComments();
        //判断操作用户是否是评论的所有者
        String commentOwner="";
        if(SecurityContextHolder.getContext().getAuthentication()!=null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
            User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(principal!=null){
                commentOwner=principal.getUsername();
            }
            if(authorityUtil.judgeAuthority(SecurityContextHolder.getContext().getAuthentication(),catalogId)){
                commentOwner="admin";
            }
        }
        model.addAttribute("commentOwner",commentOwner);
        model.addAttribute("comments",comments);
        model.addAttribute("blogModel",blog);
        return "/userspace/blog:: #mainContainerRepleace";
    }

    /**
     * 发表评论
     * @param blogId
     * @param commentContent
     * @return
     */
    @ResponseBody
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMINistrator','ROLE_ADMIN','ROLE_USER')")
    public ErrorResponse createComment(@RequestParam("blogId") Long blogId, @RequestParam("commentContent")String commentContent) throws Exception{
        blogService.createComment(blogId,commentContent);
        return new ErrorResponse(true,"发表评论成功！");
    }

    /**
     * 删除评论
     * @param commentId
     * @param blogId
     * @param catalogId
     * @return
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('ROLE_ADMINistrator','ROLE_ADMIN','ROLE_USER')")
    public ErrorResponse delete(@PathVariable("id") Long commentId, @RequestParam("blogId") Long blogId,@RequestParam("catalogId")Long catalogId)throws Exception{
        boolean isOwner =false;
        User user=commentService.getCommentById(commentId).getUser();
        //判断操作用户是否是评论的所有者
        if(SecurityContextHolder.getContext().getAuthentication()!=null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
            User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(principal!=null&&user.getUsername().equals(principal.getUsername())){
                isOwner=true;
            }else if(authorityUtil.judgeAuthority(SecurityContextHolder.getContext().getAuthentication(),catalogId)){
                isOwner=true;
            }
        }
        if(!isOwner){
           throw new Exception("此用户没有权限");
        }
        blogService.removeComment(blogId,commentId);
        commentService.removeComment(commentId);
        return new ErrorResponse(true,"删除评论成功！");
    }
}
