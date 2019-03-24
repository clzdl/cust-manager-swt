package com.clzdl.crm.springboot.auth;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;

import com.clzdl.crm.ExceptionMessage;
import com.clzdl.crm.springboot.persistence.entity.SysMenu;
import com.clzdl.crm.springboot.persistence.entity.SysRole;
import com.clzdl.crm.springboot.persistence.entity.SysUser;
import com.clzdl.crm.springboot.service.sys.ISysUserService;
import com.framework.common.exception.BizException;
import com.framework.common.util.string.StringUtil;
import com.framework.shrio.auth.AbstractShiroRealm;
import com.framework.shrio.auth.ShiroUserContext;

public class ShiroDbRealm extends AbstractShiroRealm {

	@Autowired
	private ISysUserService sysUserService;

	public ShiroDbRealm() {
	}

	@Override
	protected ShiroUserContext CreateUserContext(UsernamePasswordToken token) throws Exception {
		SysUser sysUser = sysUserService.findSysUserByLoginName(token.getUsername());
		if (sysUser == null) {
			throw new BizException(ExceptionMessage.USER_LOGIN_ERROR);
		}

		ShiroUserContext userContext = new ShiroUserContext(sysUser.getId().longValue(), sysUser.getName(),
				sysUser.getLoginPwd(), token.getHost(), sysUser.getLoginName());
		return userContext;
	}

	@Override
	protected Set<String> CreatePermissionSet(ShiroUserContext userContext) throws Exception {

		Set<String> result = new HashSet<String>();
		List<SysMenu> sysMenuList = sysUserService.listSysAllMenuByUserId(userContext.getUserId());
		if (CollectionUtils.isNotEmpty(sysMenuList)) {
			for (SysMenu menu : sysMenuList) {
				if (StringUtil.isNotBlank(menu.getHref())) {
					result.add(menu.getHref());
				}
			}
		}

		return result;
	}

	@Override
	protected Set<String> CreateRoleNameSet(ShiroUserContext userContext) throws Exception {
		Set<String> result = new HashSet<String>();
		List<SysRole> sysRoleList = sysUserService.listSysRoleByUserId(userContext.getUserId());
		if (CollectionUtils.isNotEmpty(sysRoleList)) {
			for (SysRole role : sysRoleList) {
				if (StringUtil.isNotBlank(role.getRoleName())) {
					result.add(role.getRoleName());
				}
			}
		}
		return result;
	}

}
