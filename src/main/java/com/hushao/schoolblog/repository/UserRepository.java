package com.hushao.schoolblog.repository;

import com.hushao.schoolblog.domain.Authority;
import com.hushao.schoolblog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * @author 胡少
 */
public interface UserRepository extends JpaRepository<User,Long> {
    /**
     *根据用户姓名分页查询用户列表
     * @param nickname
     * @param pageable
     * @return
     * @throws Exception
     */
    Page<User> findByNicknameLike(String nickname, Pageable pageable) throws Exception;

    /**
     * 根据用户账号查询用户
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 根据名称列表查询用户列表
     * @param usernames
     * @return
     */
    List<User> findByUsernameIn(Collection<String> usernames);

    /**
     * 根据用户昵称模糊查询管理员列表
     * @param authorities
     * @param nickname
     * @param pageable
     * @return
     * @throws Exception
     */
    Page<User> findByAuthoritiesAndNicknameLike(List<Authority> authorities,String nickname,Pageable pageable) throws Exception;
}
