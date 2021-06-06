package com.notificationsystem.notificationconsumer.consumer;

import com.google.gson.Gson;
import com.notificationsystem.notificationconsumer.model.NotificationEvent;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class NotificationEventsConsumer {


    @KafkaListener(topics = "#{'${spring.kafka.topics}'.split(',')}")
    public void onMessageListener(ConsumerRecord<Integer, String> consumerRecord) {
        log.info("Notification received, topic : {}, message : {}", consumerRecord.topic(), consumerRecord.value());
        handleNotification(consumerRecord);
    }

    private void handleNotification(ConsumerRecord<Integer, String> consumerRecord) {
        handleEmail(consumerRecord);
    }

    private void handleEmail(ConsumerRecord<Integer, String> consumerRecord) {

        Gson gson = new Gson();
        NotificationEvent event = gson.fromJson(consumerRecord.value(), NotificationEvent.class);

        Email from = new Email("dev.exg.20@gmail.com");
        String subject = "Notification from " + event.getMessage().getFrom() + " : " + event.getMessage().getSubject();
        Email to = new Email(event.getMessage().getTo());
        Content content = new Content("text/plain", event.getMessage().getBody());
        Mail mail = new Mail(from, subject, to, content);

        String apiKey = null;

        if(apiKey!=null) {
            SendGrid sg = new SendGrid(apiKey);
            Request request = new Request();
            try {
                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());
                Response response = sg.api(request);
                log.info("Email sent successfully, status code : {}, response : {}", response.getStatusCode(), response.getBody());
            } catch (IOException ex) {
                log.error("Error sending email : {}", ex.getMessage());
            }
        }
    }

}
