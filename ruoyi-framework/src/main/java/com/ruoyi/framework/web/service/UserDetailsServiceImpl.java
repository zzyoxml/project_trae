package com.ruoyi.framework.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.edu.domain.EduAppUser;
import com.ruoyi.edu.service.IEduAppUserService;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private IEduAppUserService eduAppUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        SysUser user = userService.selectUserByUserName(username);
        if (StringUtils.isNotNull(user))
        {
            if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
            {
                log.info("登录用户：{} 已被删除.", username);
                throw new ServiceException(MessageUtils.message("user.password.delete"));
            }
            else if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
            {
                log.info("登录用户：{} 已被停用.", username);
                throw new ServiceException(MessageUtils.message("user.blocked"));
            }
            passwordService.validate(user);
            return createLoginUser(user);
        }

        EduAppUser appUser = eduAppUserService.selectEduAppUserByUserName(username);
        if (StringUtils.isNotNull(appUser))
        {
            if ("1".equals(appUser.getStatus()))
            {
                log.info("登录用户：{} 已被停用.", username);
                throw new ServiceException(MessageUtils.message("user.blocked"));
            }
            if ("2".equals(appUser.getDelFlag()))
            {
                log.info("登录用户：{} 已被删除.", username);
                throw new ServiceException(MessageUtils.message("user.password.delete"));
            }

            SysUser sysUser = new SysUser();
            sysUser.setUserId(appUser.getUserId());
            sysUser.setUserName(appUser.getUsername());
            sysUser.setNickName(appUser.getNickName());
            sysUser.setEmail(appUser.getEmail());
            sysUser.setPhonenumber(appUser.getPhonenumber());
            sysUser.setAvatar(appUser.getAvatar());
            sysUser.setPassword(appUser.getPassword());
            sysUser.setStatus("0");

            passwordService.validate(sysUser);

            Set<String> permissions = new HashSet<>();
            permissions.add("*:*:*");
            return new LoginUser(appUser.getUserId(), null, sysUser, permissions);
        }

        log.info("登录用户：{} 不存在.", username);
        throw new ServiceException(MessageUtils.message("user.not.exists"));
    }

    public UserDetails createLoginUser(SysUser user)
    {
        return new LoginUser(user.getUserId(), user.getDeptId(), user, permissionService.getMenuPermission(user));
    }
}
