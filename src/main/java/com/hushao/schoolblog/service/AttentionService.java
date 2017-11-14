package com.hushao.schoolblog.service;

import com.hushao.schoolblog.domain.Attention;
import com.hushao.schoolblog.domain.User;

import java.util.List;

/**
 * @author hushao
 * @date 2017/10/26
 */
public interface AttentionService {
    /**
     * 根据用户获取关注列表
     * @param user
     * @return
     */
    List<Attention> getByUser(User user) throws Exception;


    /**
     * 根据id查询
     * @param attentionId
     * @return
     * @throws Exception
     */
    Attention getByAttentionId(Long attentionId)throws Exception;

    /**
     * 删除此用户的所有关注分类
     * @param user
     * @throws Exception
     */
    void removeAttentionByUser(User user)throws Exception;

    /**
     * 取消关注
     * @param attentionId
     * @throws Exception
     */
    void removeAttention(Long attentionId)throws Exception;
}
