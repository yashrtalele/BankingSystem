package org.example;

import com.sun.jdi.IntegerType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

public class CustomerMenuTests {
    private Customer customerA, customerB;
    private Account accountA, accountB;
    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        customerA = new Customer("jay","india","1234","jay","jay");
        customerB = new Customer("som","india","1234","som","som");
        accountA = new SavingsAccount(customerA);
        accountB = new CurrentAccount(customerB);
        customerA.addAccount(accountA);
        customerB.addAccount(accountB);
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void validDepositTest() {
        outputStream.reset();
        accountA.deposit(500);
        String output = outputStream.toString();
        Assertions.assertFalse(output.contains("Amount to deposit should be positive."));
    }

    @Test
    public void invalidDepositTest() {
        outputStream.reset();
        accountA.deposit(0);
        String output = outputStream.toString();
        Assertions.assertTrue(output.contains("Amount to deposit should be positive."));
    }

    @Test
    public void validWithdrawalTest() {
        accountA.deposit(1000);
        outputStream.reset();
        accountA.withdraw(500);
        String output = outputStream.toString();
        Assertions.assertFalse(output.contains("Insufficient balance or invalid amount."));
    }

    @Test
    public void invalidWithdrawalTest() {
        outputStream.reset();
        accountA.withdraw(500);
        String output = outputStream.toString();
        Assertions.assertTrue(output.contains("Insufficient balance or invalid amount."));
    }

    @Test
    public void validTransferTest() {
        accountA.deposit(1000);
        outputStream.reset();
        accountA.transfer(accountB, 500);
        String output = outputStream.toString();
        Assertions.assertFalse(output.contains("Insufficient balance or invalid amount."));
    }

    @Test
    public void invalidTransferTest() {
        accountA.deposit(1000);
        outputStream.reset();
        accountA.transfer(accountB, 5000);
        String output = outputStream.toString();
        Assertions.assertTrue(output.contains("Insufficient balance or invalid amount."));
    }
}
