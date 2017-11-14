package com.hushao.schoolblog.service.impl;

import com.hushao.schoolblog.domain.Attention;
import com.hushao.schoolblog.domain.Catalog;
import com.hushao.schoolblog.domain.User;
import com.hushao.schoolblog.domain.Vote;
import com.hushao.schoolblog.repository.AttentionRepository;
import com.hushao.schoolblog.repository.CatalogRepository;
import com.hushao.schoolblog.service.AttentionService;
import com.hushao.schoolblog.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author hushao
 * @date 2017/10/26
 */
@Service
public class AttentionServiceImpl implements AttentionService {
    @Autowired
    private AttentionRepository attentionRepository;
    @Override
    public List<Attention> getByUser(User user) {
        //获取用户关注列表
        return attentionRepository.getByUser(user);
    }


    @Override
    public Attention getByAttentionId(Long attentionId) throws Exception {
        return attentionRepository.getOne(attentionId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeAttention(Long attentionId) throws Exception {
        attentionRepository.delete(attentionId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeAttentionByUser(User user) throws Exception {
        attentionRepository.deleteByUser(user);
    }
}
