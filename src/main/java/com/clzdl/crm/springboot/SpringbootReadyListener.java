package com.clzdl.crm.springboot;

import java.util.concurrent.CountDownLatch;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

public class SpringbootReadyListener implements ApplicationListener<ApplicationReadyEvent> {
	public final static CountDownLatch latch = new CountDownLatch(1);

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		latch.countDown();
	}

}
