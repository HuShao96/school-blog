package com.hushao.schoolblog.repository;

import com.hushao.schoolblog.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author hushao
 * @date 2017/10/21
 */
public interface VoteRepository extends JpaRepository<Vote,Long> {
}
