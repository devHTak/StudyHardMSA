package com.example.mq;

import com.example.domain.Notification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public Notification sendNotification(String topic, Notification notification) {
        try {
            String sendMessage = objectMapper.writeValueAsString(notification);
            kafkaTemplate.send(topic, sendMessage);
            log.info("Kafka Producer Message: {}", sendMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return notification;
    }
}
