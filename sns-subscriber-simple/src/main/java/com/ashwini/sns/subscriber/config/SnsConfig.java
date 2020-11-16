package com.ashwini.sns.subscriber.config;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class SnsConfig {
	
	@Value("${cloud.aws.region.static}")
	private String awsRegion;

	@Value("${cloud.aws.credentials.access-key}")
	private String iamAccessKey;

	@Value("${cloud.aws.credentials.secret-key}")
	private String iamSecretKey;
	
	@Value("${application.sns.topic-arn}")
	private String topicArn;
	
	@Value("${application.sns.topic-name}")
	private String topicName;

	/**
	 * Async AWS SNS Client
	 * @return
	 */
	@Bean(name="amazonSNS")
	public AmazonSNSAsync asyncSnsClient() {
		return AmazonSNSAsyncClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(iamAccessKey, iamSecretKey)))
				.withRegion(awsRegion).build();
	}
	
	/**
	 * Template for sending/receiving notifications
	 * @return
	 */
	@Bean
	public NotificationMessagingTemplate notificationMessagingTemplate() {
		return new NotificationMessagingTemplate(asyncSnsClient());
	}
	
	//This is for registering subscription programmatically, not by console
	//The only issue is endpoint.
	@PostConstruct
	public void subscribeTopic() {
		SubscribeRequest subscriptionRequest = new SubscribeRequest();
		subscriptionRequest.setProtocol("http");
		subscriptionRequest.setEndpoint("http://bc5c816052fe.ngrok.io/subscribe");
		subscriptionRequest.setTopicArn("arn:aws:sns:us-east-1:101828129045:TestTopic");
		
		Future<SubscribeResult> future = asyncSnsClient().subscribeAsync(subscriptionRequest);
		
		try {
			SubscribeResult subscribeResult = future.get();
			System.out.println();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
