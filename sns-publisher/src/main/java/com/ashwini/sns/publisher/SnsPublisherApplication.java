package com.ashwini.sns.publisher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.ashwini.sns.publisher.publish.Publisher;

@SpringBootApplication
public class SnsPublisherApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(SnsPublisherApplication.class, args);
		
		Publisher publisher = context.getBean(Publisher.class);
		
		publisher.sendSampleNotification();
	}

}
