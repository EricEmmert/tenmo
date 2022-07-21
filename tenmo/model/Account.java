package com.techelevator.tenmo.model;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

public class Account {
    @NotBlank(message ="Balance must be greater than 0.")
    private double accountBalance;
    @NotBlank(message="Account requires an id.")
    private int id;

//    public Account (double accountBalance,int id){
//        this.accountBalance = accountBalance;
//        this.id=id;
//    }



    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
