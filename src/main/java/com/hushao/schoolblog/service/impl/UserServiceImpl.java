package com.hushao.schoolblog.service.impl;

import com.hushao.schoolblog.domain.Authority;
import com.hushao.schoolblog.domain.Catalog;
import com.hushao.schoolblog.domain.User;
import com.hushao.schoolblog.repository.UserRepository;
import com.hushao.schoolblog.service.AttentionService;
import com.hushao.schoolblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * 用户服务接口的实现
 * @author hushao
 */
@Service
public class UserServiceImpl implements UserService,UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AttentionService attentionService;
    @Transactional(rollbackFor = Exception.class)
    @Override
    public User saveOrUpateUser(User user) throws Exception{
        //判断是增加还是修改
        if(user.getUserId()!=null){
            //List<Catalog> catalogs=new ArrayList<>();
            //catalogs=this.getUserById(user.getUserId()).getCatalogs();
            //user.setCatalogs(catalogs);
        }
        return userRepository.save(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User registerUser(User user) throws Exception{
        return userRepository.save(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeUser(Long userId) throws Exception{
        User user=this.getUserById(userId);
        attentionService.removeAttentionByUser(user);
        userRepository.delete(user);
    }

    @Override
    public User getUserById(Long userId) throws Exception{
        return userRepository.findOne(userId);
    }

    @Override
    public Page<User> listUsersByNameLike(String nickname, Pageable pageable) throws Exception{
        //模糊查询
        nickname="%"+nickname+"%";
        Page<User> users=userRepository.findByNicknameLike(nickname,pageable);
        return users;
    }

    @Override
    public List<User> listUsersByUsernames(Collection<String> usernames) {
        return userRepository.findByUsernameIn(usernames);
    }

    @Override
    public Page<User> listAdminsByNameLike(String nickname, Pageable pageable) throws Exception {
        //模糊查询
        nickname="%"+nickname+"%";
        Authority authority=new Authority();
        authority.setId(2L);
        authority.setName("ROLE_ADMIN");
        List<Authority> authorities=new ArrayList<>();
        authorities.add(authority);
        return userRepository.findByAuthoritiesAndNicknameLike(authorities,nickname,pageable);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }
}
