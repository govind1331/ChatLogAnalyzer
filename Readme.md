
# Chat Log Analyzer

## Overview
The **Chat Log Analyzer** is a real-time chat analysis application built with Spring Boot, Kafka, and Redis. It processes chat messages streamed via Kafka, analyzes word frequencies using Java 8 Streams, caches results incrementally in Redis, and exposes them through REST APIs. This project demonstrates a scalable, event-driven architecture suitable for communication platforms, inspired by technologies relevant to companies like Sinch.

---

## Features
- **Real-Time Streaming**: Kafka streams chat messages from a producer to a consumer for immediate processing.
- **Incremental Caching**: Redis stores word frequencies, message counts, and timestamps, updated incrementally for efficiency.
- **REST API**: Exposes analysis results (top words, message count, last updated time) via intuitive endpoints.
- **String Manipulation**: Cleans and parses messages to extract meaningful word frequency data.
- **Scalable Design**: Leverages Kafka’s distributed streaming and Redis’s in-memory caching for performance.

---

## Tech Stack
- **Java 17**: Core programming language with modern features.
- **Spring Boot 3.4.4**: Framework for REST APIs, dependency injection, and application setup.
- **Apache Kafka**: Streams chat messages in real-time.
- **Redis**: Caches analysis results with a 1-hour TTL.
- **Lombok**: Reduces boilerplate code for cleaner implementation.
- **Maven**: Dependency management and build tool.

---

## Project Structure
```
chat-log-analyzer/
├── src/
│   ├── main/
│   │   ├── java/com/projectx/loganalyzer/
│   │   │   ├── ChatLogAnalyzerApplication.java  # Main entry point
│   │   │   ├── config/
│   │   │   │   ├── KafkaConfig.java           # Kafka producer setup
│   │   │   │   ├── RedisConfig.java           # Redis serialization
│   │   │   ├── controller/
│   │   │   │   ├── AnalyticsController.java  # REST endpoints
│   │   │   ├── producer/
│   │   │   │   ├── KafkaChatProducer.java    # Kafka message producer
│   │   │   ├── consumer/
│   │   │   │   ├── KafkaChatConsumer.java    # Kafka message consumer
│   │   │   ├── service/
│   │   │   │   ├── ChatAnalyzerService.java  # Analysis and caching logic
                ├── MiscService.java  # Cache retrieval logic
│   │   └── resources/
│   │       ├── application.properties        # Configuration
│   └── test/                                 # Unit tests (optional)
├── pom.xml                                   # Maven dependencies
└── README.md                                 # This file
```

---

## Prerequisites
- **Java 17**: Installed and configured (e.g., OpenJDK).
- **Maven**: For dependency management and building.
- **Docker**: To run Kafka and Redis locally.
- **Redis CLI**: Optional, for manual inspection.
- **Kafka CLI**: Optional, for topic management.

---

## Setup Instructions

### 1. Install Dependencies
- Clone the repository:
  ```
  git clone https://github.com/yourusername/chat-log-analyzer.git
  cd chat-log-analyzer
  ```
- Build the project:
  ```
  mvn clean install
  ```

### 2. Start Kafka
- Run Kafka via Docker:
  ```
  docker run -d -p 9092:9092 --name kafka apache/kafka:latest
  ```
- Create the `chat-logs` topic:
  ```
  docker exec -it kafka /opt/kafka/bin/kafka-topics.sh --create --topic chat-logs --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
  ```

### 3. Start Redis
- Run Redis via Docker:
  ```
  docker run -d -p 6379:6379 --name redis redis:latest
  ```

### 4. Configure Application
- Edit `src/main/resources/application.properties` if needed (defaults work locally):
  ```
  spring.kafka.bootstrap-servers=localhost:9092
  spring.kafka.consumer.group-id=chat-analyzer-group
  spring.kafka.consumer.auto-offset-reset=earliest
  spring.redis.host=localhost
  spring.redis.port=6379
  server.port=8080
  ```

### 5. Run the Application
- Start the Spring Boot app:
  ```
  mvn spring-boot:run
  ```
- The app will begin producing sample messages every 5 seconds and processing them.

---

## Usage

### API Endpoints
- **Send a Message**:
  ```
  curl -X POST "http://localhost:8080/analytics/send-message?user=user1&message=Hello%20world"
  ```
  - Response: `201 Created` with `"Message sent successfully"`.
- **Get Top Words**:
  ```
  curl http://localhost:8080/analytics/top-words
  ```
  - Response: `{"hello": "1", "world": "1"}` (or `404` if empty).
- **Get Full Stats**:
  ```
  curl http://localhost:8080/analytics/stats
  ```
  - Response:
    ```json
    {
      "topWords": {"hello": "2", "world": "1"},
      "messageCount": 2,
      "lastUpdated": "2025-04-08T12:34:56Z"
    }
    ```

### Redis CLI Inspection
- Check cached data:
  ```
  redis-cli
  KEYS *
  HGETALL top_words
  GET message_count
  GET last_updated
  ```

---

## How It Works
1. **Producer**: `KafkaChatProducer` sends chat messages to the `chat-logs` topic, either via API or scheduled task.
2. **Consumer**: `KafkaChatConsumer` listens to the topic and passes messages to `ChatAnalyzerService`.
3. **Analysis**: `ChatAnalyzerService`:
   - Cleans messages and counts word frequencies using Java 8 Streams.
   - Incrementally updates Redis with word counts, message count, and timestamp.
4. **Caching**: Redis stores data with a 1-hour TTL:
   - `top_words`: Hash of word frequencies.
   - `message_count`: Total messages processed.
   - `last_updated`: Last update timestamp.
5. **API**: `AnalyticsController` exposes results via REST endpoints.

---

## Skills Demonstrated
- **Kafka**: Real-time streaming with producer and consumer.
- **Redis**: Incremental caching with hashes and strings.
- **Spring Boot**: RESTful API development and dependency management.
- **Java 8**: Streams and functional programming for efficient data processing.
- **String Manipulation**: Parsing and cleaning chat messages.
- **Error Handling**: Robust serialization and API responses.
- **Scalability**: Event-driven design suitable for communication platforms.

---

## Future Enhancements
- **Top N Words**: Sort and limit to the top 5 frequent words.
- **Sliding Window**: Track word trends over a specific time period.
- **Authentication**: Add Spring Security for API access control.
- **Testing**: Include unit tests with JUnit and Testcontainers.

---

## Troubleshooting
- **Kafka Not Connecting**: Ensure `localhost:9092` is accessible and the topic exists.
- **Redis Empty**: Verify `localhost:6379` is running and data hasn’t expired (TTL = 1 hour).
- **Serialization Errors**: Confirm `RedisConfig` uses `StringRedisSerializer` for all serializers.

---

## License
MIT License - feel free to use and modify this code as needed.

---

**Author**: Govind Murali
**GitHub**: https://github.com/govind1331/ChatLogAnalyzer.git
**Date**: April 2025

---
