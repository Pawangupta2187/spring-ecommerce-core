package com.coremodule.coremodule.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TokenFailedException  extends RuntimeException {
    public TokenFailedException(String s) {
        super(s);
    }
}