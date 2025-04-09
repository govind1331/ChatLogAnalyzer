package com.projectx.loganalyzer.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.projectx.loganalyzer.producer.KafkaChatProducer;
import com.projectx.loganalyzer.service.ChatAnalyzerService;

@Controller
public class BaseController {
	
	@Autowired
	private  ChatAnalyzerService analyzerService;
	
	@Autowired
	private  KafkaChatProducer chatProducer;
	
	@GetMapping("/")
	public ResponseEntity<String> defaultMessage(){
		
		return new ResponseEntity<>("Hello World!",HttpStatus.OK);
	}
	
	@GetMapping("/top-words")
	public ResponseEntity<Map<Object,Object>> getTopWords(){
		
		Map<Object,Object> topWords = analyzerService.getTopWords();
		
		if(topWords.isEmpty()) {
			return ResponseEntity.status(404).body(null);
		}
		
		return ResponseEntity.ok(topWords);
		
	}
	
	@PostMapping("/send-message")
    public ResponseEntity<String> sendMessage(@RequestParam String user, @RequestParam String message) {
        chatProducer.sendMessage(user, message);
        return ResponseEntity.status(201).body("Message sent successfully"); // 201 Created
    }

}
