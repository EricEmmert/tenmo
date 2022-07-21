package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.NotEnoughBalanceException;
import com.techelevator.tenmo.exception.TransactionNotFoundException;
import com.techelevator.tenmo.exception.UserNotFoundException;
import com.techelevator.tenmo.model.Transfer;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

public interface TransferDao {
    List<Transfer> findAll();

    Transfer findById(Integer user_id, Integer id) throws TransactionNotFoundException;

    Transfer pendingRequests(int userId);

    List<Transfer> findByUserId(Integer id) throws UserNotFoundException;

    public int findUserIdFromName(String name) throws UserNotFoundException;

    int sendMoney(Transfer transfer) throws UserNotFoundException, NotEnoughBalanceException;

  public List<Transfer> pendingTransactions() throws TransactionNotFoundException;

  public List<Transfer> approvedTransactions() throws TransactionNotFoundException;

  public List<Transfer> rejectedTransactions() throws TransactionNotFoundException;

}
