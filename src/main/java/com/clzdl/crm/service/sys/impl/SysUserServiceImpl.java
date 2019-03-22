package com.clzdl.crm.service.sys.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.auth.shiro.ShiroKit;
import com.base.exception.BizException;
import com.base.mvc.page.PageModel;
import com.base.mvc.service.mybatis.AbastractEntityService;
import com.base.util.cipher.MD5Util;
import com.clzdl.crm.ExceptionMessage;
import com.clzdl.crm.common.persistence.entity.SysMenu;
import com.clzdl.crm.common.persistence.entity.SysRole;
import com.clzdl.crm.common.persistence.entity.SysRoleUser;
import com.clzdl.crm.common.persistence.entity.SysUser;
import com.clzdl.crm.service.sys.ISysMenuService;
import com.clzdl.crm.service.sys.ISysRoleMenuService;
import com.clzdl.crm.service.sys.ISysRoleService;
import com.clzdl.crm.service.sys.ISysUserRoleService;
import com.clzdl.crm.service.sys.ISysUserService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Builder;
import tk.mybatis.mapper.util.Sqls;

/**
 * @classname: SysUserServiceImpl
 * @description: 系统管理用户
 * @author java
 *
 */
@Service("sysUserServiceImpl")
public class SysUserServiceImpl extends AbastractEntityService<SysUser> implements ISysUserService {
	private final static Logger _logger = LoggerFactory.getLogger(SysUserServiceImpl.class);

	@Resource
	private ISysUserRoleService sysUserRoleService;
	@Resource
	private ISysRoleMenuService sysRoleMenuService;
	@Resource
	private ISysMenuService sysMenuService;
	@Resource
	private ISysRoleService sysRoleService;

	protected SysUserServiceImpl() {
		super(SysUser.class);
	}

	@Override
	@Transactional(readOnly = true)
	public PageModel<SysUser> listPageModel(SysUser entity, Integer pageNo, Integer pageSize) throws Exception {
		Builder builder = Example.builder(SysUser.class);
		if (null != entity.getId()) {
			builder.where(Sqls.custom().andEqualTo("id", entity.getId()));
		}

		builder.orderByDesc("id");
		return super.listPageModelByExample(builder.build(), pageNo, pageSize);
	}

	@Override
	public void login(String loginName, String loginPwd) throws Exception {
		try {
			Subject currentUser = ShiroKit.getSubject();
			UsernamePasswordToken token = new UsernamePasswordToken(loginName, MD5Util.MD5Encode(loginPwd));
			currentUser.login(token);
//			LogManager.me().executeLog(LogTaskFactory.bussinessLog("用户登录成功", null, null));
		} catch (IncorrectCredentialsException e) {
			ShiroKit.getSubject().logout();
			throw new BizException(ExceptionMessage.USER_LOGIN_ERROR);
		}
		_logger.info("用户:{} 登录成功", loginName);
	}

	@Override
	public SysUser findSysUserByLoginName(String loginName) throws Exception {
		return super.getByExample(
				Example.builder(SysUser.class).where(Sqls.custom().andEqualTo("loginName", loginName)).build());
	}

	@Override
	public void save(SysUser entity) throws Exception {

		if (entity.getId() != null) {
			sysUserRoleService.deleteByUserId(entity.getId());
			super.update(entity);
		} else {
			super.insert(entity);
		}

		if (CollectionUtils.isNotEmpty(entity.getUserRoleIds())) {
			for (Long roleId : entity.getUserRoleIds()) {
				sysUserRoleService.insert(entity.getId(), roleId);
			}
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<SysMenu> listSysAllMenuByUserId(Long sysUserId) throws Exception {
		List<SysRoleUser> roleUserList = sysUserRoleService.listByUserId(sysUserId);
		if (CollectionUtils.isEmpty(roleUserList)) {
			return new ArrayList<SysMenu>();
		}

		Set<Long> roleIds = new HashSet<Long>();
		for (SysRoleUser roleUser : roleUserList) {
			roleIds.add(roleUser.getSysRoleId());
		}
		Set<Long> menuIds = sysRoleMenuService.listByRoleIds(roleIds);
		if (CollectionUtils.isEmpty(menuIds)) {
			return new ArrayList<SysMenu>();
		}
		return sysMenuService.listByIds(menuIds);
	}

	@Override
	@Transactional(readOnly = true)
	public List<SysRole> listSysRoleByUserId(Long sysUserId) throws Exception {
		List<SysRoleUser> roleUserList = sysUserRoleService.listByUserId(sysUserId);
		if (CollectionUtils.isEmpty(roleUserList)) {
			return new ArrayList<SysRole>();
		}

		Set<Long> roleIds = new HashSet<Long>();
		for (SysRoleUser roleUser : roleUserList) {
			roleIds.add(roleUser.getSysRoleId());
		}
		if (CollectionUtils.isEmpty(roleIds)) {
			return new ArrayList<SysRole>();
		}
		return sysRoleService.listByIds(roleIds);
	}

	@Override
	public boolean fakeDeleteById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fakeDeleteEntity(SysUser entity) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Map<Long, SysUser> list2Map(List<SysUser> list) {
		// TODO Auto-generated method stub
		return null;
	}

}
