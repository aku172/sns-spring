package com.ashwini.sns.publisher.config;

import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSns;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.ListTopicsResult;
import com.amazonaws.services.sns.model.Topic;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class SnsConfiguration {

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
		log.info("Create AWS SNS Client");
		AmazonSNSAsync asyncClient = AmazonSNSAsyncClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(iamAccessKey, iamSecretKey)))
				.withRegion(awsRegion).build();
		
		ListTopicsResult listTopicsResult = asyncClient.listTopics();
		
		List<Topic> topics = listTopicsResult.getTopics();
		
		boolean topicExists = topics.stream().anyMatch(x -> x.getTopicArn().equalsIgnoreCase(topicArn));
		
		//Create Topic if none exists
		if(!topicExists) {
			log.info("Create topic");
			createSNSTopic(asyncClient);
		}
		
		return asyncClient;
	}
	
	private void createSNSTopic(AmazonSNSAsync asyncClient) {
		log.info("Creating a new SNS topic");
		CreateTopicRequest request = new CreateTopicRequest();
		request.setName(topicName);
		
		Future<CreateTopicResult> result = asyncClient.createTopicAsync(request);
		
	}

	/**
	 * Template for sending/receiving notifications
	 * @return
	 */
	@Bean
	public NotificationMessagingTemplate notificationMessagingTemplate() {
		return new NotificationMessagingTemplate(asyncSnsClient());
	}
	
}
