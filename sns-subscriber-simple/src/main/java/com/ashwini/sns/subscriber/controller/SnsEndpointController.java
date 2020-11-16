package com.ashwini.sns.subscriber.controller;

import org.springframework.cloud.aws.messaging.config.annotation.NotificationMessage;
import org.springframework.cloud.aws.messaging.config.annotation.NotificationSubject;
import org.springframework.cloud.aws.messaging.endpoint.NotificationStatus;
import org.springframework.cloud.aws.messaging.endpoint.annotation.NotificationMessageMapping;
import org.springframework.cloud.aws.messaging.endpoint.annotation.NotificationSubscriptionMapping;
import org.springframework.cloud.aws.messaging.endpoint.annotation.NotificationUnsubscribeConfirmationMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/subscribe")
public class SnsEndpointController {
	
	 @NotificationSubscriptionMapping
	    public void handleSubscriptionMessage(NotificationStatus notificationStatus) {
	        log.info("Handle Subscription Message...");
	        notificationStatus.confirmSubscription();
	    }

	    @NotificationMessageMapping
	    public void handleNotificationMessage(@NotificationSubject String subject,
	        @NotificationMessage String notificationMessage) {
	        log.info("Handling Notification Message...");
	        log.info("Subject: {}", subject);
	        log.info("Message: {}", notificationMessage);
	    }

	    @NotificationUnsubscribeConfirmationMapping
	    public void handleUnsubscribeMessage(NotificationStatus notificationStatus) {
	        log.info("Handling Unsubscribe Message...");
	        notificationStatus.confirmSubscription();
	    }

}
