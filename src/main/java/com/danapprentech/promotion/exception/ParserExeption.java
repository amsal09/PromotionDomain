package com.danapprentech.promotion.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.ParseException;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ParserExeption extends ParseException {
    private static final long serialVersionUID = 7718828512143293558L;
    public ParserExeption(String message){
        super(message,500);
    }
}
