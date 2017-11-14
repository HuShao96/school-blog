package com.hushao.schoolblog.service.impl;

import com.hushao.schoolblog.domain.Vote;
import com.hushao.schoolblog.repository.VoteRepository;
import com.hushao.schoolblog.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hushao
 * @date 2017/10/21
 */
@Service
public class VoteServiceImpl implements VoteService {
    @Autowired
    private VoteRepository voteRepository;
    @Override
    public Vote getVoteById(Long id) {
        return voteRepository.findOne(id);
    }

    @Override
    public void removeVote(Long id) {
        voteRepository.delete(id);
    }
}
