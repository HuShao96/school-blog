package com.hushao.schoolblog.service;

import com.hushao.schoolblog.domain.Blog;
import com.hushao.schoolblog.domain.Catalog;
import com.hushao.schoolblog.domain.User;
import com.hushao.schoolblog.domain.es.EsBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author hushao
 * @date 2017/10/19
 */
public interface BlogService {
    /**
     * 判断是增加还是修改
     * @param username
     * @param blog
     */
    void saveOrUpdateBlog(String username, Blog blog)throws Exception;

    /**
     * 保存博客
     * @param blog
     * @return
     * @throws Exception
     */
    Blog saveBlog(Blog blog) throws Exception;
    /**
     * 修改博客
     * @param blog
     * @return
     * @throws Exception
     */
    Blog updateBlog(Blog blog) throws Exception;

    /**
     * 删除博客
     * @param id
     * @throws Exception
     */
    void removeBlog(Long id) throws Exception ;

    /**
     * 根据id获取博客
     * @param id
     * @return
     * @throws Exception
     */
    Blog getBlogById(Long id) throws Exception;

    /**
     * 根据用户进行博客名称分页模糊查询（最新）
     * @param user
     * @param title
     * @param pageable
     * @return
     * @throws Exception
     */
    Page<Blog> listBlogsByUserAndTitleLikeToNewest(User user, String title, Pageable pageable) throws Exception;

    /**
     * 根据用户进行博客名称分页模糊查询（最热）
     * @param user
     * @param title
     * @param pageable
     * @return
     * @throws Exception
     */
    Page<Blog> listBlogsByUserAndTitleLikeToHottest(User user,String title,Pageable pageable) throws Exception;

    /**
     * 根据用户名分类查询
     * @param user
     * @param catalog
     * @param pageable
     * @return
     * @throws Exception
     */
    Page<Blog> listBlogsByCatalogAndUser(User user,Catalog catalog,Pageable pageable) throws Exception;

    /**
     * 阅读量递增
     * @param id
     * @throws Exception
     */
    void readingIncrease(Long id) throws Exception;

    /**
     * 发表评论
     * @param blogId
     * @param commentContent
     * @return
     * @throws Exception
     */
    Blog createComment(Long blogId,String commentContent) throws Exception;

    /**
     * 删除评论
     * @param blogId
     * @param commentId
     * @throws Exception
     */
    void removeComment(Long blogId,Long commentId) throws Exception;

    /**
     * 点赞
     * @param blogId
     * @return
     * @throws Exception
     */

    Blog createVote(Long blogId) throws Exception;

    /**
     * 取消点赞
     * @param blogId
     * @param voteId
     * @throws Exception
     */
    void removeVote(Long blogId,Long voteId) throws Exception;

    /**
     * 根据分类进行博客名称分页模糊查询（最新）
     * @param catalog
     * @param title
     * @param pageable
     * @return
     * @throws Exception
     */
    Page<Blog> listBlogsByTitleAndCatalogToNewest(Catalog catalog, String title, Pageable pageable) throws Exception;

    /**
     * 根据分类进行博客名称分页模糊查询（最热）
     * @param catalog
     * @param title
     * @param pageable
     * @return
     * @throws Exception
     */
    Page<Blog>  listBlogsByTitleAndCatalogToHottest(Catalog catalog,String title,Pageable pageable) throws Exception;

    /**
     * 根据分类分类查询
     * @param catalog
     * @param pageable
     * @return
     * @throws Exception
     */
    Page<Blog> listBlogsByCatalog(Catalog catalog,Pageable pageable) throws Exception;

    /**
     * 前五最热当前分类博客
     * @param catalog
     * @return
     * @throws Exception
     */
    List<Blog> listTop5HottestCatalogBlogs(Catalog catalog) throws Exception;

    /**
     * 前五最新当前分类博客
     * @param catalog
     * @return
     * @throws Exception
     */
    List<Blog> listTop5NewestCatalogBlogs(Catalog catalog)throws Exception;

}
