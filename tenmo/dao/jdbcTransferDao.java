package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.NotEnoughBalanceException;
import com.techelevator.tenmo.exception.TransactionNotFoundException;
import com.techelevator.tenmo.exception.UserNotFoundException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class jdbcTransferDao implements TransferDao {
    private final JdbcTemplate jdbcTemplate;

    public jdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //Need to make all Transfers Consistent
    @Override
    public List<Transfer> findAll() {
        List<Transfer> transactions = new ArrayList<>();
        String sql = "select transfer_id, amount, transfer_type_desc, transfer_status_desc, t1.username as from_user,t2.username as to_user\n" +
                "from transfer \n" +
                "join transfer_status using(transfer_status_id)\n" +
                "join transfer_type using(transfer_type_id)\n" +
                "join account as a1 on a1.account_id = account_from\n" +
                "join account as a2 on a2.account_id = account_to\n" +
                "join tenmo_user as t1 on a1.user_id = t1.user_id\n" +
                "join tenmo_user as t2 on a2.user_id = t2.user_id";

        return transactions;
    }


    //This needs to be updated to join Status and Type Tables
    @Override
    public Transfer findById(Integer user_id, Integer id) throws TransactionNotFoundException {
        String sql = "select transfer_id, amount, transfer_type_desc, transfer_status_desc, t1.username as from_user,t2.username as to_user\n" +
                "from transfer \n" +
                "join transfer_status using(transfer_status_id)\n" +
                "join transfer_type using(transfer_type_id)\n" +
                "join account as a1 on a1.account_id = account_from\n" +
                "join account as a2 on a2.account_id = account_to\n" +
                "join tenmo_user as t1 on a1.user_id = t1.user_id\n" +
                "join tenmo_user as t2 on a2.user_id = t2.user_id\n" +
                "where (t1.user_id = ? or t2.user_id = ? ) " +
                "and transfer_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, user_id, user_id, id);
        if (results.next()) {

            return mapToRowTransfer(results);
        }
        throw new TransactionNotFoundException();

    }

    public int findUserIdFromName(String name) throws UserNotFoundException {
        int user_id = 0;
        String sql = "Select user_id from tenmo_user where username = ?";
        try {
            user_id = jdbcTemplate.queryForObject(sql, int.class, name);
        } catch (Exception e) {
            throw new UserNotFoundException();
        }
        return user_id;
    }

    @Override
    public int sendMoney(Transfer transfer) throws UserNotFoundException, NotEnoughBalanceException {
        jdbcAccountDao accountDao = new jdbcAccountDao(jdbcTemplate);

        String sql = "update account set balance = balance - ? where user_id = ?;";
        double amount = transfer.getTransferAmount();
        int account_from = findUserIdFromName(transfer.getAccountFrom());
        int account_to = findUserIdFromName(transfer.getAccountTo());
        double balance = accountDao.getBalance(findUserIdFromName(transfer.getAccountFrom()));
        if (balance >= transfer.getTransferAmount()) {
            jdbcTemplate.update(sql, amount, account_from);
        } else {
            throw new NotEnoughBalanceException();
        }

        sql = "update account set balance = balance + ? where user_id = ?;";
        jdbcTemplate.update(sql, amount, account_to);
        sql = "insert into transfer(amount,transfer_type_id,transfer_status_id,account_from,account_to) values(?,2,2,?,?) returning transfer_id;";
        double ammount = transfer.getTransferAmount();
        int from_user = getAccountIdFromUserId(account_from);
        int to_user = getAccountIdFromUserId(account_to);
        return jdbcTemplate.queryForObject(sql, int.class, ammount, from_user, to_user);
    }

//    private double getBalance(int id){
//        String sql = "select balance, account_id  from account where user_id = ?";
//        Account account = jdbcTemplate.queryForObject(sql, Account.class, id);
//        return account.getAccountBalance();
//    }


    private int getAccountIdFromUserId(int user_id) {
        String sql = "Select account_id from account join tenmo_user using(user_id) where user_id = ? ";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, user_id);
        int id = 0;
        if (rs.next()) {
            id = rs.getInt("account_id");
        }
        return id;
    }



    @Override
    public List<Transfer> pendingTransactions() throws TransactionNotFoundException {
        List<Transfer> pendingTransactions = new ArrayList<>();
        String sql ="select transfer_type_id, transfer_status_id from transfer " +
                "where transfer_status_id = 1" +
                "and account_from = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,pendingTransactions);
        while(results.next()) {
            pendingTransactions.add(mapToRowTransfer(results));
        }
        return pendingTransactions;
    }


    @Override
    public List<Transfer> approvedTransactions() throws TransactionNotFoundException {
        List<Transfer> approvedTransactions = new ArrayList<>();
        String sql = "select transfer_type_id,transfer_status_id from transfer" +
                "where transfer_status_id = 2" +
                "and account_from = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,approvedTransactions);
        while(results.next()) {
            approvedTransactions.add(mapToRowTransfer(results));
        }
        return approvedTransactions;
    }


    @Override
    public List<Transfer> rejectedTransactions() throws TransactionNotFoundException {
        List<Transfer> rejectedTransactions = new ArrayList<>();
        String sql = "select transfer_type_id,transfer_status_id from transfer" +
                "where transfer_status_id =3" +
                "and account_from = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,rejectedTransactions);
        while(results.next()){
            rejectedTransactions.add(mapToRowTransfer(results));
        }
        return rejectedTransactions;
    }

    @Override
    public List<Transfer> findByUserId(Integer id) throws UserNotFoundException {
        List<Transfer> resultsList = new ArrayList<>();
        String sql = " select transfer_id, amount, transfer_type_desc, transfer_status_desc, t1.username as from_user,t2.username as to_user " +
                "from transfer " +
                "join transfer_status using(transfer_status_id) " +
                "join transfer_type using(transfer_type_id) " +
                "join account as a1 on a1.account_id = account_from " +
                "join account as a2 on a2.account_id = account_to " +
                "join tenmo_user as t1 on a1.user_id = t1.user_id " +
                "join tenmo_user as t2 on a2.user_id = t2.user_id " +
                "where t1.user_id = ? or t2.user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id, id);
        while (results.next()) {
            resultsList.add(mapToRowTransfer(results));
        }
        return resultsList;


    }

    private Transfer mapToRowTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransfer_Id(rs.getInt("transfer_id"));
        transfer.setTransferAmount(rs.getDouble("amount"));
        transfer.setTransferStatus(rs.getString("transfer_status_desc"));
        transfer.setTransferType(rs.getString("transfer_type_desc"));
        transfer.setAccountFrom(rs.getString("from_user"));
        transfer.setAccountTo(rs.getString("to_user"));

        return transfer;
    }
}
