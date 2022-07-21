package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.exception.UserNotFoundException;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(path="/account")
public class AccountController {
    private AccountDao dao;
    public AccountController(AccountDao dao){
        this.dao = dao;
    }

@RequestMapping(path="/{id}",method = RequestMethod.GET)
public Double getBalance(@PathVariable long id) throws UserNotFoundException {
        return dao.findById(id).getAccountBalance();
}

}
