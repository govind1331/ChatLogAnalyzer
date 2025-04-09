package com.projectx.loganalyzer.service;

import java.time.Instant;
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
    private static final String MESSAGE_COUNT_KEY = "message_count";
    private static final String LAST_UPDATED_KEY = "last_updated";
	
	
	public void analyzeAndCache(String message) {
		// String manipulation: Clean and split message into words
        String[] words = message.toLowerCase().replaceAll("[^a-zA-Z0-9\\s]", "").split("\\s+");

        // Java 8 Streams: Count word frequency for this message
        Map<String, Long> wordCount = Arrays.stream(words)
                .filter(word -> !word.isEmpty())
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()));

        // Incrementally update word counts in Redis
        wordCount.forEach((word, count) -> {
            redisTemplate.opsForHash().increment(TOP_WORDS_KEY, word, count);
        });

        // Increment total message count
        redisTemplate.opsForValue().increment(MESSAGE_COUNT_KEY, 1);

        // Update last updated timestamp
        String currentTime = Instant.now().toString();
        redisTemplate.opsForValue().set(LAST_UPDATED_KEY, currentTime);

        // Set expiration for all keys (1 hour)
        redisTemplate.expire(TOP_WORDS_KEY, 1, TimeUnit.HOURS);
        redisTemplate.expire(MESSAGE_COUNT_KEY, 1, TimeUnit.HOURS);
        redisTemplate.expire(LAST_UPDATED_KEY, 1, TimeUnit.HOURS);

		
	}
	
	public Map<Object,Object>  getTopWords() {
		return redisTemplate.opsForHash().entries(TOP_WORDS_KEY);
	}
	
	public Long getMessageCount() {
        Object count = redisTemplate.opsForValue().get(MESSAGE_COUNT_KEY);
        return count != null ? Long.parseLong(count.toString()) : 0L; // Parse string to Long
    }

    public String getLastUpdated() {
        Object lastUpdated = redisTemplate.opsForValue().get(LAST_UPDATED_KEY);
        return lastUpdated != null ? lastUpdated.toString() : null;
    }
    

}
