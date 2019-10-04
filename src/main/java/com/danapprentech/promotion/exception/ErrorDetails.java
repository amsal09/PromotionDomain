package com.danapprentech.promotion.exception;

import org.springframework.http.HttpStatus;

import java.util.Date;
public class ErrorDetails {
    private Date timestamp;
    private HttpStatus code;
    private String message;
    private String details;
    public ErrorDetails(Date timestamp, HttpStatus code, String message, String details) {
        super();
        this.timestamp = timestamp;
        this.code = code;
        this.message = message;
        this.details = details;
    }
    public Date getTimestamp() {
        return timestamp;
    }

    public HttpStatus getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
    public String getDetails() {
        return details;
    }
}
