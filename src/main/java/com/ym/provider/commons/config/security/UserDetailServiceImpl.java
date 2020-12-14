package com.ym.provider.commons.config.security;

import com.ym.provider.entity.UserInfo;
import com.ym.provider.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ymaster1
 * @date 2020/11/19 16:54
 * @description
 */
@Component("userDetailsService")
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserInfoMapper mapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 注入数据库即可
     *
     * @param s
     * @return 权限不能为null
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        UserInfo info = new UserInfo();
        info.setUserName(s);
        info.setStatusFlag(true);
        UserInfo user = mapper.selectOne(info);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在！");
        }
//        如果查出来的为null,可以直接抛UsernameNotFoundException异常
//        角色和权限共用GrantedAuthority接口，这里给用户可以设置权限或权限，角色需要加上前缀ROLE_
        List<GrantedAuthority> vip = AuthorityUtils.commaSeparatedStringToAuthorityList("admin, ROLE_vip");
        return new User(user.getUserName(), passwordEncoder.encode("ymaster1"), vip);
    }
}
