package com.hushao.schoolblog.service;

import com.hushao.schoolblog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import java.util.Collection;
import java.util.List;

/**
 * 用户服务接口
 * @author hushao
 */

public interface UserService {
    /**
     * 新增，编辑，保存用户
     * @param user
     * @return
     * @throws Exception
     */
    User saveOrUpateUser(User user) throws Exception;

    /**
     * 注册用户
     * @param user
     * @return
     * @throws Exception
     */
    User registerUser(User user) throws Exception;

    /**
     * 删除用户
     * @param userId
     * @throws Exception
     */
    void removeUser(Long userId) throws Exception;

    /**
     * 根据id查询用户
     * @param userId
     * @return
     * @throws Exception
     */
    User getUserById(Long userId) throws Exception;

    /**
     * 根据用户名分页模糊查询
     * @param nickname
     * @param pageable
     * @return
     * @throws Exception
     */
    Page<User> listUsersByNameLike(String nickname, Pageable pageable) throws Exception;

    /**
     * 根据用户名的集合查询 用户详细信息的集合
     * @param usernamelist
     * @return
     */
    List<User> listUsersByUsernames(Collection<String> usernamelist);

    /**
     * 根据用户名分页模糊查询权限为管理员的列表
     * @param nickname
     * @param pageable
     * @return
     * @throws Exception
     */
    Page<User> listAdminsByNameLike(String nickname, Pageable pageable) throws Exception;
}
