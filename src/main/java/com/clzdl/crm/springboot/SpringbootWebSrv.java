package com.clzdl.crm.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;

@ComponentScan(basePackages = { "com.framework",
		"com.clzdl.crm.springboot" }, excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
				SpringbootReadyListener.class }))
@SpringBootApplication
public class SpringbootWebSrv extends SpringBootServletInitializer implements Runnable {
	private String[] args = new String[] { "" };

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SpringbootWebSrv.class);
	}

	@Override
	public void run() {
		SpringApplication application = new SpringApplication(SpringbootWebSrv.class);
		application.addListeners(new SpringbootReadyListener());
		application.run(args);

	}
}
