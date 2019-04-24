package com.clzdl.crm.srv.config;

import java.sql.SQLException;
import java.util.HashMap;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.pool.DruidDataSource;
import com.framework.mybatis.config.AbstractMybatisPlusConfig;
import com.framework.mybatis.datasource.DynamicDataSource;
import com.framework.mybatis.enums.EnumDataSource;

import tk.mybatis.spring.annotation.MapperScan;

// 由于引入多数据源，所以让spring事务的aop要在多数据源切换aop的后面
@Configuration
@EnableTransactionManagement(order = 2)
@MapperScan(basePackages = { "com.clzdl.crm.srv.persistence.dao" })
public class MybatisPlusConfig extends AbstractMybatisPlusConfig {
	/**
	 * 单数据源连接池配置
	 */
	@Bean
	@Primary
	@ConditionalOnProperty(prefix = "global", name = "muti-datasource-open", havingValue = "false")
	public DruidDataSource singleDatasource() {
		return primaryDataSource();
	}

	/**
	 * 多数据源连接池配置
	 */
	@Bean
	@Primary
	@ConditionalOnProperty(prefix = "global", name = "muti-datasource-open", havingValue = "true")
	public DynamicDataSource mutiDataSource() {

		DruidDataSource primaryDataSource = primaryDataSource();

		try {
			primaryDataSource.init();
		} catch (SQLException sql) {
			sql.printStackTrace();
		}

		DynamicDataSource dynamicDataSource = new DynamicDataSource();
		HashMap<Object, Object> hashMap = new HashMap();
		hashMap.put(EnumDataSource.PRIMARY.getCode(), primaryDataSource);
		dynamicDataSource.setTargetDataSources(hashMap);
		dynamicDataSource.setDefaultTargetDataSource(primaryDataSource);
		return dynamicDataSource;
	}
}
