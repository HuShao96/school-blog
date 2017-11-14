package com.hushao.schoolblog.controller;

import com.hushao.schoolblog.domain.*;
import com.hushao.schoolblog.service.AttentionService;
import com.hushao.schoolblog.service.BlogService;
import com.hushao.schoolblog.service.CatalogService;
import com.hushao.schoolblog.service.UserService;
import com.hushao.schoolblog.util.AuthorityUtil;
import com.hushao.schoolblog.util.ExceptionHandlerAdvice;
import com.hushao.schoolblog.vo.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户主页控制器
 * @author hushao
 */
@Controller
@RequestMapping("/u")
public class UserspaceController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private CatalogService catalogService;
    @Autowired
    private AuthorityUtil authorityUtil;
    @Autowired
    private AttentionService attentionService;

    @Value("${file.server.url}")
    private String fileServerUrl;

    /**
     * 用户主页
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}")
    public String userSpace(@PathVariable("username")String username,Model model) throws Exception{
        User user= (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);
        return "redirect:/u/"+username+"/blogs";
    }

    /**
     *查询个人博客列表
     * @param username
     * @param order
     * @param catalogId 类别
     * @param keyword
     * @param async
     * @param pageIndex
     * @param pageSize
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs")
    public String listBlogsByOrder(@PathVariable("username") String username,
                                   @RequestParam(value = "order",required = false,defaultValue = "new") String order,
                                   @RequestParam(value = "catalogId",required = false) Long catalogId,
                                   @RequestParam(value = "keyword",required = false,defaultValue = "") String keyword,
                                   @RequestParam(value = "async",required = false) boolean async,
                                   @RequestParam(value = "pageIndex",required = false,defaultValue = "0")int pageIndex,
                                   @RequestParam(value = "pageSize",required = false,defaultValue = "10")int pageSize,
                                   Model model) throws Exception{
        User user= (User) userDetailsService.loadUserByUsername(username);
        Page<Blog> page =null;
        if(catalogId!=null&&catalogId>0){
            //分类查询
            Catalog catalog=catalogService.getCatalogById(catalogId);
            Pageable pageable=new PageRequest(pageIndex,pageSize);
            page=blogService.listBlogsByCatalogAndUser(user,catalog,pageable);
            order="";
        }else if (order.equals("hot")){
            //最热查询
            //根据访问量，评论量，点赞量排序：逆序
            Sort sort=new Sort(Sort.Direction.DESC,"readSize","commentSize","voteSize");
            Pageable pageable=new PageRequest(pageIndex,pageSize,sort);
            page=blogService.listBlogsByUserAndTitleLikeToHottest(user,keyword,pageable);
        }else if(order.equals("new")){
            //最新查询
            Pageable pageable=new PageRequest(pageIndex,pageSize);
            page=blogService.listBlogsByUserAndTitleLikeToNewest(user,keyword,pageable);
        }

        //当前所在页面数据列表
        List<Blog> list=page.getContent();

        model.addAttribute("user",user);
        model.addAttribute("order",order);
        model.addAttribute("catalogId",catalogId);
        model.addAttribute("keyword",keyword);
        model.addAttribute("page",page);
        model.addAttribute("blogList",list);
        return (async==true?"/userspace/u ::#mainContainerRepleace":"/userspace/u");
    }

    /**
     * 获取个人的单个博客
     * @param username
     * @param id
     * @param async
     * @param catalogId
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping("/{username}/blogs/{id}")
    public String getBlogId(@PathVariable("username")String username,
                            @PathVariable("id")Long id,
                            @RequestParam(value = "async",required = false) boolean async,
                            @RequestParam("catalogId") Long catalogId,
                            Model model)throws Exception{
        User principal=null;
        Blog blog=blogService.getBlogById(id);
        if(!async){
            //每次读取，都可认为阅读量+1
        // blogService.readingIncrease(id);
        }

        //判断操作用户是否是博客的所有者,或者是管理员
        String isBlogOwner="";
        if(SecurityContextHolder.getContext().getAuthentication()!=null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
            principal= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(principal!=null&&username.equals(principal.getUsername())){
                isBlogOwner="owner";
            }else if(authorityUtil.judgeAuthority(SecurityContextHolder.getContext().getAuthentication(),catalogId)){
                isBlogOwner="admin";
            }
        }
        //判断操作用户的点赞情况
        List<Vote> votes=blog.getVotes();
        Vote currentVote=null;
        if(principal!=null){
            for(Vote vote:votes){
                if(vote.getUser().getUsername().equals(principal.getUsername())){
                    currentVote=vote;
                    break;
                }
            }
        }
        model.addAttribute("currentVote",currentVote);
        model.addAttribute("isBlogOwner", isBlogOwner);
        model.addAttribute("blogModel",blog);
        return (async==true?"/userspace/blog ::.blog-content-container":"/userspace/blog");
    }

    /**
     * 获取新增博客的界面
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs/edit")
    public ModelAndView createBlog(@PathVariable("username")String username,Model model) throws Exception{
        User user= (User) userDetailsService.loadUserByUsername(username);
        List<Catalog> catalogs=getAttentionToCatatlog(user);
        model.addAttribute("catalogs",catalogs);
        model.addAttribute("blog",new Blog(null,null));
        //文件服务器的地址返回给客户端
        model.addAttribute("fileServerUrl",fileServerUrl);
        return new ModelAndView("/userspace/blogedit","blogModel",model);
    }

    /**
     * 获取编辑博客界面
     * @param username  用户名
     * @param blogId    博客id
     * @return
     */
    @GetMapping("/{username}/blogs/edit/{id}")
    public ModelAndView editBlog(@PathVariable("username")String username,@PathVariable("id")Long blogId,Model model) throws Exception{
        User user= (User) userDetailsService.loadUserByUsername(username);
        List<Catalog> catalogs=getAttentionToCatatlog(user);
        model.addAttribute("catalogs",catalogs);
        model.addAttribute("blog",blogService.getBlogById(blogId));
        model.addAttribute("fileServerUrl",fileServerUrl);
        return new ModelAndView("/userspace/blogedit","blogModel",model);
    }

    /**
     * 保存博客
     * @param username
     * @param blog
     * @return
     */
    @PostMapping("/{username}/blogs/edit")
    @ResponseBody
    @PreAuthorize("authentication.name.equals(#username)")
    public ErrorResponse savaBlog(@PathVariable("username")String username, @RequestBody Blog blog)throws Exception{
        if(blog.getCatalog().getId()==null){
             throw new Exception("未选择分类");
        }
        blogService.saveOrUpdateBlog(username,blog);

        String redirectUrl="/u/"+username+"/blogs/"+blog.getId();
        return new ErrorResponse(true,"处理成功！",redirectUrl);
    }

    /**
     * 删除博客
     * @param username  博客所有者
     * @param id
     * @param catalogId
     * @return
     */
    @ResponseBody
    @DeleteMapping("/{username}/blogs/{id}")
    @PreAuthorize("authentication.name.equals(#username) or hasAnyAuthority('ROLE_ADMINistrator','ROLE_ADMIN')")
    public ErrorResponse deleteBlog(@PathVariable("username") String username, @PathVariable("id") Long id,@RequestParam("catalogId") Long catalogId)throws Exception{
        String redirectUrl="";
        blogService.removeBlog(id);
        if(username.equals(SecurityContextHolder.getContext().getAuthentication().getName())){
            redirectUrl="/u/"+username+"/blogs";
        }else if(authorityUtil.judgeAuthority(SecurityContextHolder.getContext().getAuthentication(),catalogId)){
            //是管理员
            redirectUrl="/";
        }
        return new ErrorResponse(true,"删除博客成功！",redirectUrl);
    }
    /**
     * 获取个人设置页面
     * @PreAuthorize :判断是否是当前用户访问自己
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView profile(@PathVariable("username") String username, Model model)throws Exception{
        User user= (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);
        model.addAttribute("fileServerUrl",fileServerUrl);//文件服务器的地址返回给客户端
        return new ModelAndView("/userspace/profile","userModel",model);
    }

    /**
     * 保存个人设置
     * @PreAuthorize :判断是否是当前用户访问自己
     * @param username
     * @param user
     * @return
     */
    @PostMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public String saveProfile(@PathVariable("username")String username,User user)throws Exception{

        User originalUser=userService.getUserById(user.getUserId());
        originalUser.setEmail(user.getEmail());
        originalUser.setNickname(user.getNickname());
        //判断密码是否做了修改
        String rawPassword=originalUser.getPassword(); //获取原密码
        PasswordEncoder encoder=new BCryptPasswordEncoder();
        String encodePasswd=encoder.encode(user.getPassword());//加密界面密码
        boolean isMatch=encoder.matches(rawPassword,encodePasswd);//匹配
        if(!isMatch){
            originalUser.setEncodePassword(user.getPassword());
        }
        userService.saveOrUpateUser(originalUser);
        return "redirect:/u/"+username+"/profile";
    }

    /**
     * 获取编辑头像界面
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView avatar(@PathVariable("username")String username,Model model)throws Exception{
        User user= (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);
        return new ModelAndView("userspace/avatar","userModel",model);
    }

    /**
     * 保存头像
     * @param username
     * @param user
     * @return
     */
    @PostMapping("/{username}/avatar")
    @ResponseBody
    public ErrorResponse saveAvatar(@PathVariable("username")String username, @RequestBody User user)throws Exception{
        String avatarUrl=user.getAvatar();
        User originalUser=userService.getUserById(user.getUserId());
        originalUser.setAvatar(avatarUrl);
        userService.saveOrUpateUser(originalUser);
        return new ErrorResponse(true,"处理成功！",avatarUrl);

    }

    /**
     * 获取当前用户已关注分类列表
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}/catalogs")
    public String listCatalogsByUsername(@PathVariable("username")String username, Model model)throws Exception{
        User user= (User) userDetailsService.loadUserByUsername(username);
        List<Catalog> catalogs=getAttentionToCatatlog(user);
        model.addAttribute("catalogs",catalogs);
        return "userspace/u :: #catalogRepleace";
    }

    /**
     * 获取所关注的分类列表
     * @param user
     * @return
     */
    protected List<Catalog> getAttentionToCatatlog(User user) throws Exception {
        List<Attention> attentions=attentionService.getByUser(user);
        List<Catalog> catalogs=new ArrayList<>();
        for (Attention attention:attentions){
            catalogs.add(attention.getCatalog());
        }
        return catalogs;
    }

}
