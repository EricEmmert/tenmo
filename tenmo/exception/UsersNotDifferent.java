package com.techelevator.tenmo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( code = HttpStatus.BAD_REQUEST, reason = "You cannot send money to yourself.")
public class UsersNotDifferent extends Exception{
    private static final long serialVersionUID = 1L;
    public UsersNotDifferent() {super("Account not found.");}
}
