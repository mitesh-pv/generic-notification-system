package com.notificationsystem.notificationproducer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {
    private Integer notificationEventId;
    private Integer clientId;
    private Message message;
}
