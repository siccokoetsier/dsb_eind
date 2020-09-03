package dsb.web.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@Entity
public class SMEAccount extends Account{

    private String accountManager;

    public SMEAccount(int accountID, String accountNo, double balance, List<Customer> holders, String randString) {
        super(accountID, accountNo, balance, holders);
    }

    public SMEAccount() {
    }
}


