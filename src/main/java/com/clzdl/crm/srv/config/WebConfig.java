package com.clzdl.crm.srv.config;

import java.util.Properties;

import javax.servlet.MultipartConfigElement;

import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.support.spring.stat.BeanTypeAutoProxyCreator;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import com.framework.common.util.filter.xss.XssFilter;
import com.framework.common.web.listener.ConfigListener;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	/**
	 * druidServlet注册
	 */
	@Bean
	public ServletRegistrationBean<StatViewServlet> druidServletRegistration() {
		ServletRegistrationBean<StatViewServlet> registration = new ServletRegistrationBean<StatViewServlet>(
				new StatViewServlet());
		registration.addUrlMappings("/druid/*");
		return registration;
	}

	/**
	 * druid监控 配置URI拦截策略
	 * 
	 */
	@Bean
	public FilterRegistrationBean<WebStatFilter> druidStatFilter() {
		FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<WebStatFilter>(
				new WebStatFilter());
		// 添加过滤规则.
		filterRegistrationBean.addUrlPatterns("/*");
		// 添加不需要忽略的格式信息.
		filterRegistrationBean.addInitParameter("exclusions",
				"/static/*,*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid,/druid/*");
		// 用于session监控页面的用户名显示 需要登录后主动将username注入到session里
		filterRegistrationBean.addInitParameter("principalSessionName", "username");
		return filterRegistrationBean;
	}

	/**
	 * druid数据库连接池监控
	 */
	@Bean
	public DruidStatInterceptor druidStatInterceptor() {
		return new DruidStatInterceptor();
	}

	@Bean
	public JdkRegexpMethodPointcut druidStatPointcut() {
		JdkRegexpMethodPointcut druidStatPointcut = new JdkRegexpMethodPointcut();
		String patterns = "com.clzdl.crm.srv.*.service.*";
		// 可以set多个
		druidStatPointcut.setPatterns(patterns);
		return druidStatPointcut;
	}

	/**
	 * druid数据库连接池监控
	 */
	@Bean
	public BeanTypeAutoProxyCreator beanTypeAutoProxyCreator() {
		BeanTypeAutoProxyCreator beanTypeAutoProxyCreator = new BeanTypeAutoProxyCreator();
		beanTypeAutoProxyCreator.setTargetBeanType(DruidDataSource.class);
		beanTypeAutoProxyCreator.setInterceptorNames("druidStatInterceptor");
		return beanTypeAutoProxyCreator;
	}

	/**
	 * druid 为druidStatPointcut添加拦截
	 * 
	 * @return
	 */
	@Bean
	public Advisor druidStatAdvisor() {
		return new DefaultPointcutAdvisor(druidStatPointcut(), druidStatInterceptor());
	}

	/**
	 * xssFilter注册
	 */
	@Bean
	public FilterRegistrationBean<XssFilter> xssFilterRegistration() {
		FilterRegistrationBean<XssFilter> registration = new FilterRegistrationBean<XssFilter>(new XssFilter());
		registration.addUrlPatterns("/*");
		return registration;
	}

	/**
	 * ConfigListener注册
	 */
	@Bean
	public ServletListenerRegistrationBean<ConfigListener> configListenerRegistration() {
		return new ServletListenerRegistrationBean<>(new ConfigListener());
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		if (!registry.hasMappingForPattern("/web/**")) {
			registry.addResourceHandler("/web/**").addResourceLocations("classpath:/web/");
		}
		if (!registry.hasMappingForPattern("/img/**")) {
			registry.addResourceHandler("/img/**")
					.addResourceLocations("file:" + System.getProperty("user.dir") + "/img/");
		}

	}

	/**
	 * 验证码生成相关
	 */
	@Bean
	public DefaultKaptcha kaptcha() {
		Properties properties = new Properties();
		properties.put("kaptcha.border", "no");
		properties.put("kaptcha.border.color", "105,179,90");
		properties.put("kaptcha.textproducer.font.color", "blue");
		properties.put("kaptcha.image.width", "125");
		properties.put("kaptcha.image.height", "45");
		properties.put("kaptcha.textproducer.font.size", "45");
		properties.put("kaptcha.session.key", "code");
		properties.put("kaptcha.textproducer.char.length", "4");
		properties.put("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
		Config config = new Config(properties);
		DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
		defaultKaptcha.setConfig(config);
		return defaultKaptcha;
	}

	/**
	 * 文件上传配置
	 * 
	 * @return
	 */
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		// 单个文件最大
		factory.setMaxFileSize("10MB"); // KB,MB
		// 设置总上传数据总大小
		factory.setMaxRequestSize("100MB");
		return factory.createMultipartConfig();
	}
}
