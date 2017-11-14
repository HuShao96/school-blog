package com.hushao.schoolblog.util;

import com.hushao.schoolblog.domain.Catalog;
import com.hushao.schoolblog.domain.User;
import com.hushao.schoolblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * 权限
 * @author hushao
 * @date 2017/10/25
 */
@Repository
public class AuthorityUtil {
    @Autowired
    private UserService userService;
    /**
     * 是否是管理员
     * @param authentication
     * @param catalogId
     * @return
     */
    public boolean judgeAuthority(Authentication authentication,Long catalogId) throws Exception {
        Boolean isAuthority=false;
        for (GrantedAuthority grantedAuthority:authentication.getAuthorities()){
            if(grantedAuthority.getAuthority().equals("ROLE_ADMINistrator")){
                isAuthority=true;
                break;
            }
        }
        User user= (User) authentication.getPrincipal();
        User thisUser=userService.getUserById(user.getUserId());
        if(!isAuthority){
            for (Catalog catalog: thisUser.getCatalogs()){
                if(catalog.getId().equals(catalogId)){
                    isAuthority=true;
                    break;
                }
            }
        }
        return  isAuthority;
    }

}
