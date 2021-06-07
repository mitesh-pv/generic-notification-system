package com.notificationsystem.notificationproducer.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class NotificationEventExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotificationEventException.class)
    public final ResponseEntity<NotificationApiErrorResponse> handleNotificationApiErrorResponse(
            final NotificationEventException ex, final WebRequest webRequest) {

        log.error("Error validating client, status : {}, clientId : {}", ex.getStatus(), ex.getClientId());
        NotificationApiErrorResponse errorResponse =
                new NotificationApiErrorResponse("500", "Client not registered");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
