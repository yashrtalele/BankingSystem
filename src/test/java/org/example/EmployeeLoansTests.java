package org.example;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class EmployeeLoansTests {
    private BankingSystem bankingSystem;
    private Loan loan;
    private Customer customer;
    @Before
    public void setUp() {
        bankingSystem = new BankingSystem();
        customer = new Customer("jay","india","1234","jay","jay");
        bankingSystem.addCustomer(customer);
        Account account = new SavingsAccount(customer);
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


}
