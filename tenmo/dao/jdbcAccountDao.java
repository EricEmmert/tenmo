package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.UserNotFoundException;
import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;
import java.util.ArrayList;
import java.util.List;


@Component
public class jdbcAccountDao implements AccountDao {
    private final JdbcTemplate jdbcTemplate;
    public jdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    @Override
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        String sql = "select user_id,account_id,balance from account;";
        SqlRowSet results= jdbcTemplate.queryForRowSet(sql);

        while(results.next()){
            Account account = mapRowToAccount(results);
            accounts.add(account);
        }
        return accounts;
    }

    @Override
    public Account findById(long id) throws UserNotFoundException {

        String sql = "select account_id, balance from account where user_id  = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,id);
        if(results.next()){
            return mapRowToAccount(results);
        }
        throw new UserNotFoundException();
    }

    @Override
    public Double getBalance(int userId) {
        String sql = "select balance from account where user_id =?;";
        SqlRowSet results = null;
        Double balance = null;


        try{
            results = jdbcTemplate.queryForRowSet(sql, userId);
            if(results.next()){
                balance = results.getDouble("balance");
            }

        }catch (Exception e){
            System.out.println("Cannot access.");
        }
        return balance;
    }





    public Account amountToAdd(Double amountToAdd, int id) throws AccountNotFoundException {
        String sql = "update account set balance = balance + ? where user_id =?;";

       SqlRowSet results = jdbcTemplate.queryForRowSet(sql,amountToAdd,id);
        if(results.next()){
            return mapRowToAccount(results);
        }
        throw new AccountNotFoundException();
    }


     public Account amountToSubtract(Double amountToSubtract, int id) throws AccountNotFoundException {
        String sql = "update accounts set balance = balance - ? where user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,amountToSubtract,id);
        if(results.next()){
            return mapRowToAccount(results);
        }
        throw new AccountNotFoundException();

    }

    @Override
    public int findIdByUserName(String userName) {
        String sql ="Select account_id,balance from account " +
                "join tenmo_user using(user_id) " +
                "where username =  ?;";
        Integer id = jdbcTemplate.queryForObject(sql,Integer.class,userName);
        if (id!=null){
            return id;
        }
        else {
            return 0;
        }
    }
    private Account mapRowToAccount(SqlRowSet rs){
        Account account = new Account();
        account.setAccountBalance(rs.getDouble("balance"));
        account.setId((int) rs.getLong("account_id"));
        return account;
    }

}
