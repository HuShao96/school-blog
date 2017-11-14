package com.hushao.schoolblog.service;

import com.hushao.schoolblog.domain.User;
import com.hushao.schoolblog.domain.es.EsBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author hushao
 * @date 2017/10/30
 */
public interface EsBlogService {
    /**
     * 删除EsBlog
     * @param id
     */
    void removeEsBlog(String id) throws Exception;

    /**
     * 更新EsBlog
     * @param esBlog
     * @return
     */
    EsBlog updateEsBlog(EsBlog esBlog) throws Exception;

    /**
     * 根据Blog的Id获取EsBlog
     * @param blogId
     * @return
     */
    EsBlog getEsBlogByBlogId(Long blogId);

    /**
     * 最新博客列表。分页
     * @param keyword
     * @param pageable
     * @return
     */
    Page<EsBlog> listNewestEsBlogs(String keyword, Pageable pageable);

    /**
     * 最热博客列表，分页
     * @param keyword
     * @param pageable
     * @return
     */
    Page<EsBlog> listHottestEsBlogs(String keyword,Pageable pageable);

    /**
     * 博客列表，分页
     * @param pageable
     * @return
     */
    Page<EsBlog> listEsBlogs(Pageable pageable);

    /**
     * 最新前5的博客
     * @return
     */
    List<EsBlog> listTop5NewestEsBlogs();

    /**
     * 最热前5的博客
     * @return
     */
    List<EsBlog> listTop5HottestEsBlogs();

    /**
     * 最热前10用户
     * @return
     */
    List<User> listTop10Users();
}
