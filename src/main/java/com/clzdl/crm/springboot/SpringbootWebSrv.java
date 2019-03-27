package com.clzdl.crm.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;

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

	@Bean
	public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
		return factory -> {
			ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/web/index.html");
			factory.addErrorPages(error404Page);
		};
	}
}
