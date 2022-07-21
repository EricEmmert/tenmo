package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.exception.*;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path="transfers/")
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private TransferDao dao;

    public TransferController(TransferDao transferDao){dao = transferDao;}

    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<Transfer> getAllTransfers(Principal principal) throws UserNotFoundException {
        return dao.findByUserId(dao.findUserIdFromName(principal.getName()));
    }

    @RequestMapping(path="/{id}",method = RequestMethod.GET)
    public Transfer getTransferById(Principal principal, @PathVariable int id) throws TransactionNotFoundException, UserNotFoundException {
        return dao.findById(dao.findUserIdFromName(principal.getName()),id);
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    public int sendMoney(Principal principal, @Valid @RequestBody Transfer transfer, BindingResult result) throws UserNotFoundException, UsersNotDifferent, NotEnoughBalanceException
    {
        UsersAreDifferentValidator validator = new UsersAreDifferentValidator();
        validator.validate(transfer,result);
        if(!result.hasErrors()) {
            return dao.sendMoney(transfer);
        } else {
            throw new UsersNotDifferent();
        }
    }

}