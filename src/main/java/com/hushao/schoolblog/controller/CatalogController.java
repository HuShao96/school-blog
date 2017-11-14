package com.hushao.schoolblog.controller;
import com.hushao.schoolblog.domain.*;
import com.hushao.schoolblog.domain.es.EsBlog;
import com.hushao.schoolblog.service.BlogService;
import com.hushao.schoolblog.service.CatalogService;
import com.hushao.schoolblog.service.ProvinceService;
import com.hushao.schoolblog.util.AuthorityUtil;
import com.hushao.schoolblog.vo.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author hushao
 * @date 2017/10/23
 */
@Controller
@RequestMapping("/catalogs")
public class CatalogController {
    @Autowired
    private CatalogService catalogService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private ProvinceService provinceService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private AuthorityUtil authorityUtil;

    /**
     * 根据名称模糊查询分类
     * @param catalogName
     * @param async
     * @param pageIndex
     * @param pageSize
     * @param model
     * @return
     */
    @GetMapping
    public String listCatalogs( @RequestParam(value = "catalogName",required = false,defaultValue = "") String catalogName,
                                @RequestParam(value = "async",required = false) boolean async,
                                @RequestParam(value = "pageIndex",required = false,defaultValue = "0")int pageIndex,
                                @RequestParam(value = "pageSize",required = false,defaultValue = "10")int pageSize,
                                Model model) throws Exception{
        Pageable pageable=new PageRequest(pageIndex,pageSize);
        Page<Catalog> page=catalogService.listCatalogByName(catalogName,pageable);
        List<Catalog> catalogs=page.getContent();
        model.addAttribute("page",page);
        model.addAttribute("catalogs",catalogs);
        return (async==true?"/admins/list ::#mainContainerRepleace":"/catalogs/list");
    }


    /**
     * 创建分类
     * @param catalog
     * @param provinceId
     * @return
     */
    @PostMapping
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('ROLE_ADMINistrator')")
    public ErrorResponse createCatalos(Catalog catalog, @RequestParam("provinceId") Long provinceId) throws Exception{
        catalogService.saveCatalog(provinceId,catalog);
        return new ErrorResponse(true,"创建分类成功");
    }

    /**
     * 删除分类
     * @param catalogId
     * @return
     */
    @ResponseBody
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMINistrator')")
    public ErrorResponse delete(@PathVariable("id") Long catalogId) throws Exception{
        catalogService.removeCatalog(catalogId);
        return new ErrorResponse(true,"删除分类成功！");
    }

    /**
     * 获取分类添加页面
     * @param model
     * @return
     */
    @GetMapping("/add")
    public String createForm(Model model)throws Exception{
        Catalog catalog=new Catalog(null);
        List<Province> provinces=provinceService.listPrivinces();
        model.addAttribute("catalog",catalog);
        model.addAttribute("provinces",provinces);
        return "catalogs/add";
    }

    /**
     * 根据Id获取分类编辑页面
     * @param catalogId
     * @param model
     * @return
     */
    @GetMapping("/edit/{id}")
    public String getCatalogById(@PathVariable("id") Long catalogId,Model model)throws Exception{
        Catalog catalog=catalogService.getCatalogById(catalogId);
        List<Province> provinces=provinceService.listPrivinces();
        model.addAttribute("catalog",catalog);
        model.addAttribute("provinces",provinces);
        return "catalogs/edit";
    }

    /**
     * 获取当前分类的所有博客
     * @param catalogId
     * @return
     * @throws Exception
     */
    @GetMapping("/{id}/blogs")
    public String getCatalogByIdToBlogs(@PathVariable("id") Long catalogId,
                                        @RequestParam(value = "order",required = false,defaultValue = "new") String order,
                                        @RequestParam(value = "keyword",required = false,defaultValue = "") String keyword,
                                        @RequestParam(value = "async",required = false) boolean async,
                                        @RequestParam(value = "pageIndex",required = false,defaultValue = "0")int pageIndex,
                                        @RequestParam(value = "pageSize",required = false,defaultValue = "10")int pageSize,
                                        Model model) throws Exception{
        Catalog catalog=catalogService.getCatalogById(catalogId);
        Page<Blog> page =null;
        Boolean isAuthority=false;
        if (order.equals("hot")){
            //最热查询
            //根据访问量，评论量，点赞量排序：逆序
            Sort sort=new Sort(Sort.Direction.DESC,"readSize","commentSize","voteSize");
            Pageable pageable=new PageRequest(pageIndex,pageSize,sort);
            page=blogService.listBlogsByTitleAndCatalogToHottest(catalog,keyword,pageable);
        }else if(order.equals("new")){
            //最新查询
            Pageable pageable=new PageRequest(pageIndex,pageSize);
            page=blogService.listBlogsByTitleAndCatalogToNewest(catalog,keyword,pageable);
        }
        //当前所在页面数据列表
        List<Blog> list=page.getContent();
        //判断操作用户的关注情况
        User principal=null;
        List<Attention> attentions=catalog.getAttentions();
        if(SecurityContextHolder.getContext().getAuthentication()!=null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
            principal= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            isAuthority=authorityUtil.judgeAuthority(SecurityContextHolder.getContext().getAuthentication(),catalogId);
        }

        Attention currentAttention=null;
        if(principal!=null){
            for(Attention attention:attentions){
                if(attention.getUser().getUsername().equals(principal.getUsername())){
                    currentAttention=attention;
                    break;
                }
            }
        }

        model.addAttribute("isAuthority",isAuthority);
        model.addAttribute("blogSize",list.size());
        model.addAttribute("currentAttention",currentAttention);
        model.addAttribute("catalog",catalog);
        model.addAttribute("page",page);
        model.addAttribute("blogList",list);
        model.addAttribute("order",order);
        if(!async){
            //最新文章
            List<Blog> newest=blogService.listTop5NewestCatalogBlogs(catalog);
            model.addAttribute("newest",newest);
            //热门文章
            List<Blog> hottest=blogService.listTop5HottestCatalogBlogs(catalog);
            model.addAttribute("hottest",hottest);
            //最热用户
//            List<User> users=catalogService.listTop5Users(catalog);
//            model.addAttribute("users",users);
        }
        return (async==true?"/catalogsBlog ::#mainContainerRepleace":"/catalogsBlog");
    }
    /**
     * 获取编辑分类图片界面
     * @param catalogId
     * @param model
     * @return
     */
    @GetMapping("/{catalogId}/picture")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMINistrator','ROLE_ADMIN')")
    public ModelAndView avatar(@PathVariable("catalogId")Long catalogId, Model model)throws Exception{
        Catalog catalog=catalogService.getCatalogById(catalogId);
        model.addAttribute("catalog",catalog);
        return new ModelAndView("catalogs/picture","userModel",model);
    }
    /**
     * 保存分类头像
     * @param catalogId
     * @param catalogPicture
     * @return
     */
    @PostMapping("/{catalogId}/picture")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('ROLE_ADMINistrator','ROLE_ADMIN')")
    public ErrorResponse saveAvatar(@PathVariable("catalogId")Long catalogId ,@RequestParam("catalogPicture") String catalogPicture)throws Exception{
        if(!authorityUtil.judgeAuthority(SecurityContextHolder.getContext().getAuthentication(),catalogId)){
            new ErrorResponse(false,"没有权限！");
        }
        String avatarUrl=catalogPicture;
        Catalog originaCatalog=catalogService.getCatalogById(catalogId);
        originaCatalog.setCatalogPicture(avatarUrl);
        catalogService.saveOrUpdateCatalog(originaCatalog);
        return new ErrorResponse(true,"处理成功！",avatarUrl);
    }
}
