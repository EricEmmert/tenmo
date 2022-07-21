package com.techelevator.tenmo.exception;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class UsersAreDifferentValidator implements Validator {

        @Override
        public boolean supports(Class thisClass) {
            return com.techelevator.tenmo.model.Transfer.class.equals(thisClass);
        }

        @Override
        public void validate(Object target, Errors errors) {
            Transfer transfer = (Transfer) target;

            if(transfer.getAccountTo() == transfer.getAccountFrom()) {
                errors.rejectValue("to_user", "You cannot send money to yourself");
            }
        }
}
