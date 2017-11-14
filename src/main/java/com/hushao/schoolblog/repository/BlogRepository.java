package com.hushao.schoolblog.repository;

import com.hushao.schoolblog.domain.Blog;
import com.hushao.schoolblog.domain.Catalog;
import com.hushao.schoolblog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author hushao
 * @date 2017/10/19
 */
public interface BlogRepository extends JpaRepository<Blog,Long> {
    /**
     * 根据用户名，博客标题分页查询博客列表
     * @param user
     * @param title
     * @param pageable
     * @return
     * @throws Exception
     */
    Page<Blog> findByUserAndTitleLike(User user, String title, Pageable pageable) throws Exception;

    /**
     * 根据用户名，博客查询博客列表(时间逆序)
     * @param title
     * @param user
     * @param pageable
     * @return
     * @throws Exception
     */
    Page<Blog> findByUserAndTitleLikeOrderByCreateTimeDesc(User user,String title,Pageable pageable) throws Exception;

    /**
     * 根据用户名分类查询博客列表
     * @param user
     * @param catalog
     * @param pageable
     * @return
     * @throws Exception
     */
    Page<Blog> findByUserAndCatalog(User user,Catalog catalog,Pageable pageable) throws Exception;
    /**
     * 根据分类，博客标题分页查询博客列表
     * @param catalog
     * @param title
     * @param pageable
     * @return
     * @throws Exception
     */
    Page<Blog> findByCatalogAndTitleLike(Catalog catalog, String title, Pageable pageable) throws Exception;

    /**
     * 根据分类，博客查询博客列表(时间逆序)
     * @param title
     * @param catalog
     * @param pageable
     * @return
     * @throws Exception
     */
    Page<Blog> findByCatalogAndTitleLikeOrderByCreateTimeDesc(Catalog catalog,String title,Pageable pageable) throws Exception;
    /**
     * 根据分类查询博客列表
     * @param catalog
     * @param pageable
     * @return
     * @throws Exception
     */
    Page<Blog> findByCatalog(Catalog catalog,Pageable pageable) throws Exception;
}
