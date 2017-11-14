package com.hushao.schoolblog.service;

import com.hushao.schoolblog.domain.Comment;

/**
 * @author hushao
 * @date 2017/10/20
 */
public interface CommentService {
    /**
     * 根据id查询Comment
     * @param id
     * @return
     * @throws Exception
     */
    Comment getCommentById(Long id) throws Exception;

    /**
     * 根据id删除评论
     * @param id
     * @throws Exception
     */
    void removeComment(Long id) throws Exception;
}
