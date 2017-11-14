package com.hushao.schoolblog.service;

import com.hushao.schoolblog.domain.Vote;

/**
 * @author hushao
 * @date 2017/10/21
 */
public interface VoteService {
    /**
     * 根据id获取Vote
     * @param id
     * @return
     * @throws Exception
     */
    Vote getVoteById(Long id) throws Exception;

    /**
     * 删除Vote
     * @param id
     * @throws Exception
     */
    void removeVote(Long id) throws Exception;
}
