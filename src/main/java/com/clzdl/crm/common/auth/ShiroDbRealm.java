package com.clzdl.crm.common.auth;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authc.UsernamePasswordToken;

import com.base.auth.shiro.AbastractShiroRealm;
import com.base.auth.usercontext.BaseUserContext;
import com.base.auth.usercontext.SysCurrentUserContext;
import com.base.exception.BizException;
import com.base.util.string.StringUtil;
import com.clzdl.crm.ExceptionMessage;
import com.clzdl.crm.common.persistence.entity.SysMenu;
import com.clzdl.crm.common.persistence.entity.SysRole;
import com.clzdl.crm.common.persistence.entity.SysUser;
import com.clzdl.crm.service.sys.ISysUserService;

public class ShiroDbRealm extends AbastractShiroRealm {

	private ISysUserService sysUserService;

	public ShiroDbRealm(ISysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}

	@Override
	protected BaseUserContext CreateUserContext(UsernamePasswordToken token) throws Exception {
		SysUser sysUser = sysUserService.findSysUserByLoginName(token.getUsername());
		if (sysUser == null) {
			throw new BizException(ExceptionMessage.USER_LOGIN_ERROR);
		}

		SysCurrentUserContext userContext = new SysCurrentUserContext(sysUser.getId().longValue(), sysUser.getName(),
				sysUser.getLoginPwd(), token.getHost(), 0L, sysUser.getUserType().intValue(), null);
		userContext.setLoginAccount(sysUser.getLoginName());
		return userContext;
	}

	@Override
	protected Set<String> CreatePermissionSet(BaseUserContext userContext) throws Exception {

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
	protected Set<String> CreateRoleNameSet(BaseUserContext userContext) throws Exception {
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
