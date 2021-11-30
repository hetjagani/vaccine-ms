package com.cmpe275.vms.exception;

import org.springframework.http.HttpStatus;

public class Error {
    private HttpStatus status;
    private String message;
    private Integer code;

    public Error() {}

    public Error(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.code = status.value();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
