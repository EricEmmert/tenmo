package com.techelevator.tenmo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( code = HttpStatus.BAD_REQUEST, reason = "Not Enough Money.")
public class NotEnoughBalanceException extends Exception{
    private static final long serialVersionUID = 1L;
    public NotEnoughBalanceException() {super("Not Enough Money");}
}
