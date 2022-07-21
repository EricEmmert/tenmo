package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.UserNotFoundException;
import com.techelevator.tenmo.model.Account;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

public interface AccountDao {

    List<Account> findAll();


    Double getBalance(int userId);

    Account amountToAdd(Double amountToAdd, int id) throws AccountNotFoundException;

    Account amountToSubtract(Double amountToSubtract, int id) throws AccountNotFoundException;

    int findIdByUserName(String name);


    Account findById(long id) throws UserNotFoundException;






