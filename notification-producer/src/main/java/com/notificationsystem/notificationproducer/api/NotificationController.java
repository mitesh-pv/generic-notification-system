package com.notificationsystem.notificationproducer.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.notificationsystem.notificationproducer.exception.NotificationApiErrorResponse;
import com.notificationsystem.notificationproducer.exception.NotificationEventException;
import com.notificationsystem.notificationproducer.model.NotificationEvent;
import com.notificationsystem.notificationproducer.model.RegisteredClient;
import com.notificationsystem.notificationproducer.producer.NotificationEventProducer;
import com.notificationsystem.notificationproducer.repository.RegisteredClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/notification-system")
public class NotificationController {

    @Autowired
    private NotificationEventProducer notificationEventProducer;

    @Autowired
    private RegisteredClientRepository clientRepository;

    @PostMapping("/v1/new-event")
    public ResponseEntity<?> createNewNotificationEvent(@RequestBody NotificationEvent notificationEvent) throws JsonProcessingException, ExecutionException, InterruptedException {

        RegisteredClient client = clientRepository.findByClientId(notificationEvent.getClientId());

        if(client == null) {
            throw new NotificationEventException(500);
        }

        String topic = client.getTopicName();
        notificationEvent.getMessage().setSentTime(LocalDateTime.now().toString());
        notificationEventProducer.sendNotificationEvent(notificationEvent, topic);
        return ResponseEntity.status(HttpStatus.CREATED).body("SUCCESS");
    }
}
