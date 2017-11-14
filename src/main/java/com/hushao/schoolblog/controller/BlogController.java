package com.hushao.schoolblog.controller;

import com.hushao.schoolblog.domain.Catalog;
import com.hushao.schoolblog.domain.User;
import com.hushao.schoolblog.domain.es.EsBlog;
import com.hushao.schoolblog.service.CatalogService;
import com.hushao.schoolblog.service.EsBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

/**
 * Blog控制器
 * @author hushao
 */
@Controller
@RequestMapping("/blogs")
public class BlogController {
    @Autowired
    private EsBlogService esBlogService;
    @Autowired
    private CatalogService catalogService;
    /**
     *
     * @param order  排序
     * @param keyword 关键字
     * @return
     */
    @GetMapping
    public String listBlogs(@RequestParam(value = "order",required = false,defaultValue = "new") String order,
                            @RequestParam(value = "keyword",required = false,defaultValue = "") String keyword,
                            @RequestParam(value = "async",required = false) boolean async,
                            @RequestParam(value = "pageIndex",required = false,defaultValue = "0")int pageIndex,
                            @RequestParam(value = "pageSize",required = false,defaultValue = "10")int pageSize,
                            Model model) throws Exception{
        Page<EsBlog> page=null;
        List<EsBlog> list=null;
        //系统初始化时没有博客数据
        boolean isEmpty=true;
        if (order.equals("hot")) {
            //最热查询
            Sort sort = new Sort(Sort.Direction.DESC, "readSize", "commentSize", "voteSize", "createTime");
            Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
            page = esBlogService.listHottestEsBlogs(keyword, pageable);
        } else if (order.equals("new")) {
            //最新
            Sort sort = new Sort(Sort.Direction.DESC, "createTime");
            Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
            page = esBlogService.listNewestEsBlogs(keyword, pageable);
        }
        isEmpty = false;

        list=page.getContent();
        model.addAttribute("order",order);
        model.addAttribute("keword",keyword);
        model.addAttribute("page",page);
        model.addAttribute("blogList",list);
        //首次访问页面才加载
        if(!async&&!isEmpty){
            List<EsBlog> newest=esBlogService.listTop5NewestEsBlogs();
            model.addAttribute("newest",newest);
            List<EsBlog> hottest=esBlogService.listTop5HottestEsBlogs();
            model.addAttribute("hottest",hottest);
            List<User> users=esBlogService.listTop10Users();
            model.addAttribute("users",users);
            List<Catalog> catalogs=catalogService.listCatalogHottestTop5();
            model.addAttribute("catalogs",catalogs);
        }
        return (async==true?"/index ::#mainContainerRepleace":"/index");
    }
}
