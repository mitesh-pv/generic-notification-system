package com.notificationsystem.notificationproducer.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class NotificationEventExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotificationEventException.class)
    public final ResponseEntity<NotificationApiErrorResponse> handleEmailErrorResponse(
            final NotificationEventException ex, final WebRequest webRequest) {

        NotificationApiErrorResponse errorResponse =
                new NotificationApiErrorResponse("500", "Client not registered");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
