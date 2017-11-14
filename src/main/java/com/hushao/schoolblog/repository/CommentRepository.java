package com.hushao.schoolblog.repository;

import com.hushao.schoolblog.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Comment评论仓库
 * @author hushao
 * @date 2017/10/20
 */
public interface CommentRepository extends JpaRepository<Comment,Long> {
}
