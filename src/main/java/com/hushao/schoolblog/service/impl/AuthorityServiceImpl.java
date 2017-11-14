package com.hushao.schoolblog.service.impl;

import com.hushao.schoolblog.domain.Authority;
import com.hushao.schoolblog.repository.AuthorityRepository;
import com.hushao.schoolblog.service.AuthoritySerivice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hushao
 */

@Service
public class AuthorityServiceImpl implements AuthoritySerivice {
    @Autowired
    private AuthorityRepository authorityRepository;
    @Override
    public Authority getAuthorityById(Long id) throws Exception{
        return authorityRepository.findOne(id);
    }
}
