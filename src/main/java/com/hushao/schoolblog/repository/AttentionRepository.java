package com.hushao.schoolblog.repository;

import com.hushao.schoolblog.domain.Attention;
import com.hushao.schoolblog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author hushao
 * @date 2017/10/26
 */
public interface AttentionRepository extends JpaRepository<Attention,Long> {
    /**
     * 获取用户关注列表
     * @param user
     * @return
     */
    List<Attention> getByUser(User user);
    /**
     * 删除次用户的全部关注分类
     * @param user
     */
    void deleteByUser(User user) throws Exception;
}
