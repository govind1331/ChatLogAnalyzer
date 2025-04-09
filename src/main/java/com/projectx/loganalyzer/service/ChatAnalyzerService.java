package com.projectx.loganalyzer.service;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatAnalyzerService {
	
	@Autowired
	private  RedisTemplate<String,Object> redisTemplate;
	
	private static final String TOP_WORDS_KEY ="top_words";
	
	
	public void analyzeAndCache(String message) {
		// String manipulation: Split message into words, remove punctuation
		String[] words = message.toLowerCase().replaceAll("[^a-zA-Z0-9\\s]", "").split("\\s+");
		
		// Java 8 Streams: Count word frequency
		Map<String, Long> wordCount = Arrays.stream(words)
				.filter(word -> !word.isEmpty())
				.collect(Collectors.groupingBy(word -> word, Collectors.counting()));
		
		// Convert Long to String for Redis
        Map<String, String> stringWordCount = wordCount.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()));
        redisTemplate.opsForHash().putAll(TOP_WORDS_KEY, stringWordCount);
        redisTemplate.expire(TOP_WORDS_KEY, 1, TimeUnit.HOURS);

		
	}
	
	public Map<Object,Object>  getTopWords() {
		return redisTemplate.opsForHash().entries(TOP_WORDS_KEY);
	}
}
