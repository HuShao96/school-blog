package com.hushao.schoolblog.service.impl;

import com.hushao.schoolblog.domain.*;
import com.hushao.schoolblog.domain.es.EsBlog;
import com.hushao.schoolblog.repository.BlogRepository;
import com.hushao.schoolblog.service.BlogService;
import com.hushao.schoolblog.service.EsBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Blog 服务
 * @author hushao
 * @date 2017/10/19
 */
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private EsBlogService esBlogService;
    private static final Pageable TOP_5_PAGEABLE = new PageRequest(0, 5);
    private static final String EMPTY_KEYWORD = "";

    @Override
    public void saveOrUpdateBlog(String username, Blog blog) throws Exception {
        Blog orginalBlog=null;
        //判断是修改还是增加
        if(blog.getId()!=null&&blog.getId().equals("")){
            orginalBlog=this.getBlogById(blog.getId());
            orginalBlog.setTitle(blog.getTitle());
            orginalBlog.setContent(blog.getContent());
            orginalBlog.setHtmlContent(blog.getHtmlContent());
            orginalBlog.setCatalog(blog.getCatalog());
            this.updateBlog(orginalBlog);
        }else {
            User user= (User) userDetailsService.loadUserByUsername(username);
            blog.setUser(user);
            this.saveBlog(blog);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Blog saveBlog(Blog blog) throws Exception {
        Blog returnBlog=blogRepository.save(blog);
        EsBlog esBlog=new EsBlog(returnBlog);
        esBlogService.updateEsBlog(esBlog);
        return returnBlog;
    }

    @Override
    public Blog updateBlog(Blog blog) throws Exception {
        Blog returnBlog =blogRepository.save(blog);
        EsBlog esBlog=esBlogService.getEsBlogByBlogId(returnBlog.getId());
        esBlog.update(returnBlog);
        esBlogService.updateEsBlog(esBlog);
        return returnBlog;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeBlog(Long id) throws Exception{
        blogRepository.delete(id);
        EsBlog esBlog=esBlogService.getEsBlogByBlogId(id);
        esBlogService.removeEsBlog(esBlog.getId());

    }

    @Override
    public Blog getBlogById(Long id) throws Exception {
        return blogRepository.findOne(id);
    }

    @Override
    public Page<Blog> listBlogsByUserAndTitleLikeToNewest(User user, String title, Pageable pageable)throws Exception {
        //最新
        title="%"+title+"%";
        return blogRepository.findByUserAndTitleLikeOrderByCreateTimeDesc(user,title,pageable);
    }

    @Override
    public Page<Blog> listBlogsByUserAndTitleLikeToHottest(User user, String title, Pageable pageable)throws Exception {
        //最热
        title="%"+title+"%";
        return blogRepository.findByUserAndTitleLike(user, title, pageable);
    }

    @Override
    public Page<Blog> listBlogsByCatalogAndUser(User user,Catalog catalog,Pageable pageable) throws Exception {

        return blogRepository.findByUserAndCatalog(user,catalog,pageable);
    }

    @Override
    public void readingIncrease(Long id) throws Exception{
        Blog blog=blogRepository.findOne(id);
        blog.setReadSize(blog.getReadSize()+1);
        this.updateBlog(blog);
    }

    @Override
    public Blog createComment(Long blogId, String commentContent) throws Exception{
        Blog originalBlog=blogRepository.findOne(blogId);
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment=new Comment(user,commentContent);

        List<Comment> comments=originalBlog.getComments();
        comments.add(comment);
        originalBlog.setComments(comments);

        return this.updateBlog(originalBlog);
    }

    @Override
    public void removeComment(Long blogId, Long commentId) throws Exception{
        Blog originalBlog=blogRepository.findOne(blogId);
        List<Comment> comments=originalBlog.getComments();

        for(int index=0;index<comments.size();index++){
            if(comments.get(index).getId().equals(commentId)){
                comments.remove(index);
                break;
            }
        }
        originalBlog.setComments(comments);
        this.updateBlog(originalBlog);
    }

    @Override
    public Blog createVote(Long blogId) throws Exception{
        Blog originalBlog=blogRepository.findOne(blogId);
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Vote vote=new Vote(user);
        //判断用户是否点过赞
        boolean isExist =false;
        for (int i=0;i<originalBlog.getVotes().size();i++){
            if(originalBlog.getVotes().get(i).getUser().getUserId().equals(vote.getUser().getUserId())){
                isExist=true;
                break;
            }
        }
        if(isExist){
            throw new IllegalArgumentException("该用户已点过赞了！");
        }else {
            List<Vote> votes=originalBlog.getVotes();
            votes.add(vote);
            originalBlog.setVotes(votes);
        }
        return this.updateBlog(originalBlog);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeVote(Long blogId, Long voteId) throws Exception{
        Blog originalBlog=blogRepository.findOne(blogId);
        //判断用户是否点过赞
        for (int index=0; index < originalBlog.getVotes().size(); index ++ ) {
            if (originalBlog.getVotes().get(index).getId().equals(voteId)) {
                List<Vote> votes=originalBlog.getVotes();
                votes.remove(index);
                originalBlog.setVotes(votes);
                this.updateBlog(originalBlog);
                break;
            }
        }

    }

    @Override
    public Page<Blog> listBlogsByTitleAndCatalogToNewest(Catalog catalog, String title, Pageable pageable) throws Exception {
        //当前分类最新博客
        title="%"+title+"%";
        return  blogRepository.findByCatalogAndTitleLikeOrderByCreateTimeDesc(catalog,title,pageable);
    }

    @Override
    public Page<Blog> listBlogsByTitleAndCatalogToHottest(Catalog catalog, String title, Pageable pageable) throws Exception {
        //当前分类最热博客
        title="%"+title+"%";
        Sort sort=new Sort(Sort.Direction.DESC,"readSize","commentSize","voteSize","createTime");
        if(pageable.getSort()==null){
            pageable=new PageRequest(pageable.getPageNumber(),pageable.getPageSize(),sort);
        }
        return blogRepository.findByCatalogAndTitleLike(catalog,title,pageable);
    }

    @Override
    public Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable) throws Exception {
        return blogRepository.findByCatalog(catalog,pageable);
    }

    @Override
    public List<Blog> listTop5HottestCatalogBlogs(Catalog catalog) throws Exception {
        Page<Blog> page=this.listBlogsByTitleAndCatalogToHottest(catalog,EMPTY_KEYWORD,TOP_5_PAGEABLE);
        return page.getContent();
    }

    @Override
    public List<Blog> listTop5NewestCatalogBlogs(Catalog catalog) throws Exception {
        Page<Blog> page=this.listBlogsByTitleAndCatalogToNewest(catalog,EMPTY_KEYWORD,TOP_5_PAGEABLE);
        return page.getContent();
    }



}
