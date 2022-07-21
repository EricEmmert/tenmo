package com.techelevator.tenmo.model;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class Transfer {
    @Positive(message = "Transfer must be greater than 0.")
    private Double amount;
    private Integer transfer_id;
    @NotBlank(message = "Transfer must have a type.")
    private String transfer_type_desc;
    @NotBlank(message = "Transfer must have a status.")
    private String transfer_status_desc;
    @NotBlank(message = "Transfer must contain an account from.")
    private String from_user;
    @NotBlank(message = "Transfer must contain an account to.")
    private String to_user;

    public Double getTransferAmount() {
        return amount;
    }

    public void setTransferAmount(Double transferAmount) {
        this.amount = transferAmount;
    }

    public Integer getTransfer_Id() {
        return transfer_id;
    }

    public void setTransfer_Id(Integer transfer_id) {this.transfer_id = transfer_id;}

    public String getTransferType() {
        return transfer_type_desc;
    }

    public void setTransferType(String transfer_type_desc) {
        this.transfer_type_desc = transfer_type_desc;
    }

    public String getTransferStatus() {
        return transfer_status_desc;
    }

    public void setTransferStatus(String transfer_status_desc) {
        this.transfer_status_desc = transfer_status_desc;
    }

    public String getAccountFrom() {
        return from_user;
    }

    public void setAccountFrom(String from_user) {
        this.from_user = from_user;
    }

    public String getAccountTo() {
        return to_user;
    }

    public void setAccountTo(String to_user) {
        this.to_user = to_user;
    }

}
