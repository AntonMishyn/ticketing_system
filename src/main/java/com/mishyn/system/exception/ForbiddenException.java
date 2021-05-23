package com.mishyn.system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.ServletException;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ForbiddenException extends ServletException {
    public ForbiddenException(String message) {
        super(message);
    }
}
