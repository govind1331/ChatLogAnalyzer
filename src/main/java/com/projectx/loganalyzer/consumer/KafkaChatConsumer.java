package com.projectx.loganalyzer.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.projectx.loganalyzer.service.ChatAnalyzerService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KafkaChatConsumer {
	
	@Autowired
	private  ChatAnalyzerService analyzerService;
	
	@KafkaListener(topics = "chat-logs", groupId="chat-analyzer-group")
	public void listen(String message) {
		
		analyzerService.analyzeAndCache(message);
	}
	
	

}
