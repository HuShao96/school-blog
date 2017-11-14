package com.hushao.schoolblog.repository;

import com.hushao.schoolblog.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Authority权限的仓库
 * @author 胡少
 */
public interface AuthorityRepository extends JpaRepository<Authority,Long> {
}
