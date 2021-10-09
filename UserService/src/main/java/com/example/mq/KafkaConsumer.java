package com.example.mq;

import com.example.entity.account.Account;
import com.example.entity.account.AccountZone;
import com.example.entity.notification.Notification;
import com.example.entity.notification.RequestNotification;
import com.example.service.AccountRepository;
import com.example.service.AccountTagRepository;
import com.example.service.AccountZoneRepository;
import com.example.service.NotificationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class KafkaConsumer {

    private final ObjectMapper objectMapper;
    private final NotificationRepository notificationRepository;
    private final AccountRepository accountRepository;

    @KafkaListener(topics = "study-notification-topic", groupId = "consumer_notification_group")
    public void getNotification(String kafkaMessage) {
        log.info("Kafka Consumer Messasge: {}", kafkaMessage);

        try {
            RequestNotification requestNotification = objectMapper.readValue(kafkaMessage, RequestNotification.class);
            List<Account> accounts = new ArrayList<>();
            if(requestNotification.getZone() == null && requestNotification.getTag() != null) {
                // tag에 대한 처리
                accounts = accountRepository.findByTagId(requestNotification.getTag().getId());
            } else {
                accounts = accountRepository.findByZoneId(requestNotification.getZone().getId());
            }

            log.info("Kafka Message: {}", kafkaMessage);
            log.info(requestNotification.getStudyId());

            accounts.stream().forEach(account -> {
                Notification notification = new Notification();
                notification.setAccount(account);
                notification.setMessage(requestNotification.getMessage());
                notification.setTag(requestNotification.getTag());
                notification.setZone(requestNotification.getZone());
                notification.setStudyId(requestNotification.getStudyId());

                notificationRepository.save(notification);
            });
        } catch (Exception e) {
            log.error("{} Exception: {}", this.getClass(), e.getMessage());
        }
    }
}
