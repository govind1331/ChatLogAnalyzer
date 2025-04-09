package com.projectx.loganalyzer.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MiscService {
	
	@Autowired
	private  ChatAnalyzerService analyzerService;
	
	
    public ResponseEntity<Map<String, Object>> getStats() {
    	try {
        Map<Object, Object> topWords = analyzerService.getTopWords();
        Long messageCount = analyzerService.getMessageCount();
        String lastUpdated = analyzerService.getLastUpdated();
        
    	

        Map<String, Object> stats = Map.of(
            "topWords", topWords,
            "messageCount", messageCount,
            "lastUpdated", lastUpdated
        );
        return ResponseEntity.ok(stats);
    	}catch (Exception e) {

    		System.err.println("Error fetching stats: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
    	}
    }

}
