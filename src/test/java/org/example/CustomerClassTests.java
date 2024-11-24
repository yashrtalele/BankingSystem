package org.example;

import com.sun.jdi.IntegerType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

public class CustomerClassTests {
    private Customer customer;
    private Account account;
    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        customer = new Customer("jay","india","1234","jay","jay");
        System.setOut(new PrintStream(outputStream));
        customer.applyForLoan(3500);
        account = new SavingsAccount(customer);
        customer.addAccount(account);
    }

    @Test
    public void validGetNameTest() {
        Assertions.assertEquals("jay", customer.getName());
    }

    @Test
    public void invalidGetNameTest() {
        Assertions.assertNotEquals("jaya", customer.getName());
    }

    @Test
    public void validGetAddressTest() {
        Assertions.assertEquals("india", customer.getAddress());
    }

    @Test
    public void validGetPhoneNumberTest() {
        Assertions.assertEquals("1234", customer.getPhoneNumber());
    }

    @Test
    public void validGetUsernameTest() {
        Assertions.assertEquals("jay", customer.getUsername());
    }

    @Test
    public void validGetPasswordTest() {
        Assertions.assertEquals("jay", customer.getPassword());
    }

    @Test
    public void addAccountTest() {
        Customer customer = new Customer("Jay Paul","India","1234","jaypaul","jaypaul");
        Account account = new CurrentAccount(customer);
        customer.addAccount(account);
        Assertions.assertEquals(1, customer.getAccounts().size());
    }

    @Test
    public void applyForLoanTest() {
        outputStream.reset();
        customer.applyForLoan(4500);
        String output = outputStream.toString();
        Assertions.assertTrue(output.contains("Loan application for 4500.0 submitted."));
    }

    @Test
    public void getLoansTest() {
        customer.applyForLoan(5500);
        customer.applyForLoan(350);
        customer.applyForLoan(500);

        Assertions.assertEquals(4, customer.getLoans().size());
    }

    @Test
    public void getExistingLoansTest() {
        List< Loan > loans = customer.getExistingLoans();
        loans.get(0).approveLoan();
        customer.payOffLoan(1656,3500);
        Assertions.assertEquals(0, customer.getExistingLoans().size());
    }

    @Test
    public void payOffLoansNotApprovedTest() {
        outputStream.reset();
        List< Loan > loans = customer.getLoans();
        Integer id = loans.get(0).getLoanId();
        Double loanAmount = loans.get(0).getLoanAmount();
        customer.payOffLoan(id, loanAmount);
        String output = outputStream.toString();
        Assertions.assertTrue(output.contains("Loan is not yet approved!"));
    }

    @Test
    public void payoffLoanNotFoundTest() {
        outputStream.reset();
        customer.payOffLoan(1656,3500);
        String output = outputStream.toString();
        Assertions.assertTrue(output.contains("Loan not found."));
    }

    @Test
    public void payoffLoanApprovedTest() {
        customer.applyForLoan(4500);
        List< Loan > loans = customer.getLoans();
        loans.get(0).approveLoan();
        Integer id = loans.get(0).getLoanId();
        Double loanAmount = loans.get(0).getLoanAmount();
        customer.payOffLoan(id, loanAmount);
        String output = outputStream.toString();
        Assertions.assertTrue(output.contains("Loan paid off!"));
    }

    @Test
    public void getOlderLoansTest() {
        List< Loan > loans = customer.getLoans();
        loans.get(0).approveLoan();
        Integer id = loans.get(0).getLoanId();
        Double loanAmount = loans.get(0).getLoanAmount();
        customer.payOffLoan(id, loanAmount);
        Assertions.assertEquals(1, customer.getOlderLoans().size());
    }

    @Test
    public void validViewAccountTest() {
        outputStream.reset();
        customer.viewAccount();
        String output = outputStream.toString();
        Assertions.assertTrue(output.contains("Account Number:"));
        Assertions.assertTrue(output.contains("Balance:"));
        Assertions.assertTrue(output.contains("Owner:"));
    }

    @Test
    public void validViewTransactionsTest() {
        outputStream.reset();
        customer.viewTransactions();
        String output = outputStream.toString();
        Integer accountNumber = customer.getAccounts().get(0).getAccountNumber();
        Assertions.assertTrue(output.contains("Transaction history for account " + accountNumber));
    }
}
