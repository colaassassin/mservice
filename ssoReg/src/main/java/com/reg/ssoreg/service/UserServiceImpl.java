package com.reg.ssoreg.service;

import com.google.common.collect.Lists;
import com.reg.ssoreg.constant.MessageConstant;
import com.reg.ssoreg.pojo.SecurityUser;
import com.reg.ssoreg.pojo.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService {

    private List<UserDto> userList;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initData(){
        String password = passwordEncoder.encode("123456");
        userList = new ArrayList<>();
        userList.add(new UserDto(1L,"Mike", password,1, Lists.newArrayList("ADMIN")));
        userList.add(new UserDto(2L,"AnRan", password,1, Lists.newArrayList("TEST")));
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        List<UserDto> userDtoList = userList.stream().filter(e -> e.getUsername().equals(username)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(userDtoList)) {
            throw new UsernameNotFoundException(MessageConstant.USERNAME_PASSWORD_ERROR);
        }
        SecurityUser securityUser = new SecurityUser(userDtoList.get(0));
        if (!securityUser.isEnabled()) {
            throw new DisabledException(MessageConstant.ACCOUNT_DISABLED);
        } else if (!securityUser.isAccountNonLocked()) {
            throw new LockedException(MessageConstant.ACCOUNT_LOCKED);
        } else if (!securityUser.isAccountNonExpired()) {
            throw new AccountExpiredException(MessageConstant.ACCOUNT_EXPIRED);
        } else if (!securityUser.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException(MessageConstant.CREDENTIALS_EXPIRED);
        }

        return securityUser;
    }
}
