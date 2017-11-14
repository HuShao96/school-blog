package com.hushao.schoolblog.service;

import com.hushao.schoolblog.domain.Authority;

/**
 * @author hushao
 */
public interface AuthoritySerivice {
    /**
     * 根据ID查询Authority
     * @param id
     * @return
     * @throws Exception
     */
    Authority getAuthorityById(Long id) throws Exception;
}
