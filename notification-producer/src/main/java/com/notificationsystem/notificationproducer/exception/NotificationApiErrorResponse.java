package com.notificationsystem.notificationproducer.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class NotificationApiErrorResponse {

    private String errorCode;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timeStamp;

    public NotificationApiErrorResponse(final String errorCode, final String message){
        super();
        this.errorCode = errorCode;
        this.message = message;
        this.timeStamp = LocalDateTime.now();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

}
