package org.example;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

public class EmployeeLoansTests {
    private BankingSystem bankingSystem;
    private Loan loan;
    private Customer customer;
    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    @Before
    public void setUp() {
        bankingSystem = new BankingSystem();
        customer = new Customer("jay","india","1234","jay","jay");
        bankingSystem.addCustomer(customer);
        Account account = new SavingsAccount(customer);
        customer.addAccount(account);
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void testUnapproveLoan() {
        customer.applyForLoan(1000);
        Assertions.assertEquals(1,bankingSystem.getUnapprovedLoans().size());
    }

    @Test
    public void invalidTestUnapproveLoan() {
        customer.applyForLoan(1000);
        customer.applyForLoan(1200);
        Assertions.assertNotEquals(1,bankingSystem.getUnapprovedLoans().size());
    }

    @Test
    public void testIsUnapprovedLoan() {
        customer.applyForLoan(1000);
        Assertions.assertFalse(customer.getLoans().getFirst().isApproved());
    }

    @Test
    public void testOlderLoans() {
        BankingSystem bankingSystem1 = new BankingSystem();
        Customer customer1 = new Customer("jay","india","1234","jay","jay");
        customer1.addAccount(new SavingsAccount(customer1));
        bankingSystem1.addCustomer(customer1);
        customer1.applyForLoan(1000);
        Loan newLoan = bankingSystem1.getUnapprovedLoans().get(0);
        newLoan.approveLoan();
        customer1.payOffLoan(newLoan.getLoanId(), 1000);
        Assertions.assertEquals(1,bankingSystem1.getClosedLoans().size());
    }

    @Test
    public void invalidTestOlderLoans() {
        BankingSystem bankingSystem1 = new BankingSystem();
        Customer customer1 = new Customer("jay","india","1234","jay","jay");
        customer1.addAccount(new SavingsAccount(customer1));
        bankingSystem1.addCustomer(customer1);
        customer1.applyForLoan(1000);
        Loan newLoan = bankingSystem1.getUnapprovedLoans().get(0);
        newLoan.approveLoan();
        customer1.payOffLoan(newLoan.getLoanId(), 1000);
        Assertions.assertNotEquals(2,bankingSystem1.getClosedLoans().size());
    }

    @Test
    public void testLoans() {
        BankingSystem bankingSystem1 = new BankingSystem();
        Customer customer1 = new Customer("jay","india","1234","jay","jay");
        customer1.addAccount(new SavingsAccount(customer1));
        bankingSystem1.addCustomer(customer1);
        customer1.applyForLoan(1000);
        Loan newLoan = bankingSystem1.getUnapprovedLoans().get(0);
        newLoan.approveLoan();
        Assertions.assertTrue(newLoan.isApproved());
    }

    @Test
    public void testEmptyLoans() {
        BankingSystem bankingSystem = new BankingSystem();
        Customer customer = new Customer("jay","india","1234","jay","jay");
        customer.addAccount(new SavingsAccount(customer));
        Assertions.assertTrue(bankingSystem.getUnapprovedLoans().isEmpty());
    }

    @Test
    public void testInvalidPayoffLoanAmount() {
        customer.applyForLoan(1000.0);
        customer.getLoans().getFirst().approveLoan();
        outputStream.reset();
        customer.payOffLoan(customer.getLoans().getFirst().getLoanId(), 10000.0);
        String output = outputStream.toString();
        Assertions.assertTrue(output.contains("Payoff amount exceeded"));
    }

    @Test
    public void testCloseLoan() {
        customer.applyForLoan(1000.0);
        customer.getLoans().getLast().approveLoan();
        customer.payOffLoan(customer.getLoans().getLast().getLoanId(), 1000.0);
        Assertions.assertTrue(customer.getLoans().getLast().isClosed());
    }
}
