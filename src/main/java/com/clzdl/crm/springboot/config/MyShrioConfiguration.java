package com.clzdl.crm.springboot.config;

import java.util.Map;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.servlet.Cookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.clzdl.crm.springboot.auth.ShiroDbRealm;
import com.framework.shrio.config.ShiroConfiguration;

@Configuration
public class MyShrioConfiguration extends ShiroConfiguration {

	@Override
	@Bean
	public AuthorizingRealm myShiroRealm() {
		return new ShiroDbRealm();
	}

	//// 配置缓存管理器
	@Bean
	public CacheManager cacheManager() {
		return super.cacheManager();
	}

	//// 会话ID生成器
	@Bean
	public SessionIdGenerator sessionIdGenerator() {
		return super.sessionIdGenerator();
	}

	//// 会话Cookie模板，使用sid存储sessionid
	@Bean
	public Cookie sessionIdCookie() {
		return super.sessionIdCookie();
	}

	//// 会话DAO
	@Bean
	public SessionDAO sessionDAO() {
		return super.sessionDAO();
	}

	//// 会话管理器
	@Bean
	public SessionManager sessionManager() {
		return super.sessionManager(sessionDAO(), sessionIdCookie());
	}

	@Bean
	public AuthenticatingFilter formAuthenticationFilter() {
		return super.formAuthenticationFilter();
	}

	/// rememberMe管理器
	@Bean
	public Cookie rememberMeCookie() {
		return super.rememberMeCookie();
	}

	/// rememberMe管理器
	@Bean
	public RememberMeManager rememberMeManager() {
		return super.rememberMeManager(rememberMeCookie());
	}

	// 权限管理，配置主要是Realm的管理认证
	@Bean
	public SecurityManager securityManager() {
		return super.securityManager(myShiroRealm(), cacheManager(), sessionManager(), rememberMeManager());
	}

	// 加入注解的使用，不加入这个注解不生效
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
		return super.authorizationAttributeSourceAdvisor(securityManager());
	}

	// Filter工厂，设置对应的过滤条件和跳转条件
	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilterFactoryBean() {
		return super.shiroFilterFactoryBean(securityManager());
	}

	/**
	 * 在方法中 注入 securityManager,进行代理控制
	 */
	@Bean
	public MethodInvokingFactoryBean methodInvokingFactoryBean() {
		return super.methodInvokingFactoryBean(securityManager());
	}

	/**
	 * 保证实现了Shiro内部lifecycle函数的bean执行
	 */
	@Bean
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	/**
	 * 启用shrio授权注解拦截方式，AOP式方法级权限检查
	 */
	@Bean
	@DependsOn(value = "lifecycleBeanPostProcessor") // 依赖其他bean的初始化
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		return new DefaultAdvisorAutoProxyCreator();
	}

	@Override
	protected void addFilterChainDefinitionMap(Map<String, String> map) {
		map.put("/panel/profile/sysuser/login.json", "anon");
	}
}
