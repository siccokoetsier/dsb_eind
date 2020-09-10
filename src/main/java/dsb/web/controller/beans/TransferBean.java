package dsb.web.controller.beans;

import dsb.web.controller.AccountOverviewController;
import dsb.web.domain.Account;
import dsb.web.service.validators.CurrencyFormatConstraint;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 *  This bean is used for currency transfers that have not yet been validated.
 *  All field validation happens in this bean, providing error messages to the front-end if validation fails.
 *  If validation is successful, a Transaction object can be create for storage in the database.
 */

public class TransferBean {
    private Logger logger = LoggerFactory.getLogger(AccountOverviewController.class);

    private Account debitAccount;

    //@NotBlank(message = "Vul een tegenrekening in")
    // TODO: Werkend krijgen @AccountNoConstraint
    //@DSBAccountConstraint
    private String creditAccountNo;


    @NotBlank(message = "Voer een bedrag in")
    @CurrencyFormatConstraint
    private String transferAmountString;

    @Positive(message = "Voer een bedrag groter dan 0 in")
    @Digits(integer = 50, fraction = 2, message = "Voer maximaal twee cijfers achter de komma in")
    private BigDecimal transferAmount;

    @AssertTrue(message = "Onvoldoende saldo voor transactie")
    private boolean sufficientFunds;

    @Length(max = 50, message = "Maximaal 50 karakters")
    private String message;

    public TransferBean(Account debitAccount) {
        this.debitAccount = debitAccount;
    }

    public TransferBean() {
    }

    // Parse BigDecimal from transferAmountString
    private void parseTransferAmount() {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.GERMAN);
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        decimalFormat.setParseBigDecimal(true);
        BigDecimal parsedAmount = null;
        try {
            parsedAmount = (BigDecimal) decimalFormat.parse(transferAmountString.trim());
            transferAmount = parsedAmount;
//            System.out.println("Account is: " + debitAccount.getBalance());
        } catch (ParseException parseError) {
            transferAmount = null;
        }
    }

    // Compare parsed BigDecimal transferAmount to accountbalance
    private void checkSufficientFunds() {
        // TODO: WAT IS HIER GAANDE???
        logger.debug("Account in Transferbean:" + debitAccount.toString());
        System.out.println("IK KOM HIER");
        setSufficientFunds(debitAccount.getBalance() > transferAmount.doubleValue());
    }

    public Account getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(Account debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getCreditAccountNo() {
        return creditAccountNo;
    }

    public void setCreditAccountNo(String creditAccountNo) {
        this.creditAccountNo = creditAccountNo;
    }

    public String getTransferAmountString() {
        return transferAmountString;
    }

    public void setTransferAmountString(String transferAmountString) {
        this.transferAmountString = transferAmountString;
        parseTransferAmount();
        checkSufficientFunds();
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public boolean isSufficientFunds() {
        return sufficientFunds;
    }

    public void setSufficientFunds(boolean sufficientFunds) {
        this.sufficientFunds = sufficientFunds;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "TransferBean{" +
                "debitAccount=" + debitAccount +
                ", creditAccountNo='" + creditAccountNo + '\'' +
                ", transferAmount=" + transferAmount +
                ", sufficientFunds=" + sufficientFunds +
                ", message='" + message + '\'' +
                '}';
    }
}
