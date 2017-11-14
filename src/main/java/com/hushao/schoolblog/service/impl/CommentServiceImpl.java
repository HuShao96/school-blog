package com.hushao.schoolblog.service.impl;

import com.hushao.schoolblog.domain.Comment;
import com.hushao.schoolblog.repository.CommentRepository;
import com.hushao.schoolblog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author hushao
 * @date 2017/10/20
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment getCommentById(Long id) throws Exception{
        return commentRepository.findOne(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeComment(Long id) throws Exception{
        commentRepository.delete(id);
    }
}
