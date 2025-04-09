package com.projectx.loganalyzer.producer;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class KafkaChatProducer {
	
	
	private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "chat-logs";
    private int messageCounter = 0;
    

    public KafkaChatProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String user, String message) {
        kafkaTemplate.send(TOPIC, user, message);
    }
	
    @Scheduled(fixedRate = 5000) // Send every 5 seconds
    public void simulateChat() {
        String user = "user" + (messageCounter % 5); // Rotate 5 users
        String message = "Hello world, this is message " + messageCounter++;
        sendMessage(user, message);
        System.out.println("Sent: " + message);
    }

}
