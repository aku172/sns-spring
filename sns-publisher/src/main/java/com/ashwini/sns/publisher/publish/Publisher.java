package com.ashwini.sns.publisher.publish;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Publisher {
	
	@Value("${application.sns.topic-name}")
	private String topicName;
	
	@Autowired
	public NotificationMessagingTemplate notificationMessagingTemplate;
	
	public void sendSampleNotification() {
		log.info("Sending a sample Hello World notification");
		
		String message = "Hello World!!!!!";
		
		notificationMessagingTemplate.sendNotification(topicName, message, "Test Notification");
				
	}

}
