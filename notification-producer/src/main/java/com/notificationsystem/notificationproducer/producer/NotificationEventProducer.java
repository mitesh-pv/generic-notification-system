package com.notificationsystem.notificationproducer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notificationsystem.notificationproducer.model.NotificationEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;

@Component
@Slf4j
public class NotificationEventProducer {

    @Autowired
    private KafkaTemplate<Integer, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendNotificationEvent(NotificationEvent notificationEvent, String topic) throws JsonProcessingException {
        Integer key = notificationEvent.getNotificationEventId();
        String val = objectMapper.writeValueAsString(notificationEvent);

        ProducerRecord<Integer, String> producerRecord = buildProducerRecord(key, val, topic);
        ListenableFuture<SendResult<Integer, String>> listenableFuture = kafkaTemplate.send(producerRecord);
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onFailure(Throwable throwable) {
                handleFailure(key, val, throwable);
            }

            @Override
            public void onSuccess(SendResult<Integer, String> integerStringSendResult) {
                handleSuccess(key, val, integerStringSendResult);
            }
        });
    }

    private ProducerRecord<Integer, String> buildProducerRecord(Integer key, String val, String topic) {
        List<Header> recordHeaders = List.of(new RecordHeader("event-source", "scanner".getBytes()));
        return new ProducerRecord<>(topic, null, key, val, recordHeaders);
    }

    private void handleFailure(Integer key, String val, Throwable throwable) {
        log.error("Error sending message, exception : {}", throwable.getMessage());
        try{
            throw throwable;
        }catch (Throwable throwable1) {
            log.error("Error in onFailure : {}", throwable1.getMessage());
        }
    }

    private void handleSuccess(Integer key, String val, SendResult<Integer, String> integerStringSendResult) {
        log.info("Message sent successfully for key : {} and value : {}, partition is : {} ",
                key, val, integerStringSendResult.getRecordMetadata().partition());
    }
}
