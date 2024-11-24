package org.example;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class AdminMenuTests {
    private BankingSystem bankingSystem;
    private Customer customer;
    private Employee employee;
    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        bankingSystem = new BankingSystem();
        customer = new Customer("jay","india","1234","jay","jay");
        bankingSystem.addCustomer(customer);
        employee = new Employee("manan","1234","manan","manan");
        bankingSystem.addEmployee(employee);
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void validRegister(){
        Customer newCust = bankingSystem.verify("som","india","1234","som","som");
        bankingSystem.addCustomer(newCust);
        Assertions.assertEquals(2, bankingSystem.getCustomers().size());
    }

    @Test
    public void emptyNameRegister(){
        Customer customer1 = bankingSystem.verify("","india","1234","som","som");
        Assertions.assertNull(customer1);
    }

    @Test
    public void emptyPhoneRegister(){
        Customer customer1 = bankingSystem.verify("som","india","","som","som");
        Assertions.assertNull(customer1);
    }

    @Test
    public void emptyUsernameRegister(){
        Customer customer1 = bankingSystem.verify("som","india","1234","","som");
        Assertions.assertNull(customer1);
    }

    @Test
    public void duplicateUsernameRegister(){
        Customer customer1 = bankingSystem.verify("som","india","1234","jay","som");
        Assertions.assertNull(customer1);
    }

    @Test
    public void emptyPasswordRegister(){
        Customer customer1 = bankingSystem.verify("som","india","1234","som","");
        Assertions.assertNull(customer1);
    }

    @Test
    public void validAccountRegister(){
        Account account = bankingSystem.verifyAccount(customer,1);
        Assertions.assertNotNull(account);
    }

    @Test
    public void invalidAccountRegister(){
        Account account = bankingSystem.verifyAccount(customer,3);
        Assertions.assertNull(account);
    }

    @Test
    public void validAddCustomer(){
        Customer customer1 = new Customer("som","india","1234","som","som");
        bankingSystem.addCustomer(customer1);
        Assertions.assertEquals(2, bankingSystem.getCustomers().size());
    }

    @Test
    public void invalidAddCustomer(){
        Customer customer1 = null;
        bankingSystem.addCustomer(customer1);
        Assertions.assertEquals(1, bankingSystem.getCustomers().size());
    }

    @Test
    public void validEmployeeRegister(){
        Employee employee1 = bankingSystem.verifyEmployee("som","1234","som","som");
        bankingSystem.addEmployee(employee1);
        Assertions.assertEquals(2, bankingSystem.getEmployees().size());
    }

    @Test
    public void emptyNameEmployeeRegister(){
        Employee employee1 = bankingSystem.verifyEmployee("","1234","som","som");
        Assertions.assertNull(employee1);
    }

    @Test
    public void emptyPhoneEmployeeRegister(){
        Employee employee1 = bankingSystem.verifyEmployee("som","","som","som");
        Assertions.assertNull(employee1);
    }

    @Test
    public void emptyUsernameEmployeeRegister(){
        Employee employee1 = bankingSystem.verifyEmployee("som","1234","","som");
        Assertions.assertNull(employee1);
    }

    @Test
    public void duplicateUsernameEmployeeRegister(){
        Employee employee1 = bankingSystem.verifyEmployee("som","1234","manan","som");
        Assertions.assertNull(employee1);
    }

    @Test
    public void emptyPasswordEmployeeRegister(){
        Employee employee1 = bankingSystem.verifyEmployee("som","1234","som","");
        Assertions.assertNull(employee1);
    }

    @Test
    public void validAddEmployee(){
        Employee employee1 = new Employee("som","1234","som","som");
        bankingSystem.addEmployee(employee1);
        Assertions.assertEquals(2, bankingSystem.getEmployees().size());
    }

    @Test
    public void invalidAddEmployee(){
        Employee employee1 = null;
        bankingSystem.addEmployee(employee1);
        Assertions.assertEquals(1, bankingSystem.getEmployees().size());
    }

    @Test
    public void validInterestOnSavingsAccount(){
        outputStream.reset();
        Account account = bankingSystem.verifyAccount(customer,1);
        account.deposit(1000);
        account.applyInterest();
        String output = outputStream.toString();
        Assertions.assertTrue(output.contains("Interest applied"));
    }

    @Test
    public void validInterestOnCurrentAccount(){
        outputStream.reset();
        Account account = bankingSystem.verifyAccount(customer,2);
        account.deposit(1000);
        account.applyInterest();
        String output = outputStream.toString();
        Assertions.assertTrue(output.contains("No interest on current accounts"));
    }


    @Test
    public void invalidInterest(){
        outputStream.reset();
        Account account = bankingSystem.verifyAccount(customer,2);
        account.deposit(1000);
        account.applyInterest();
        String output = outputStream.toString();
        Assertions.assertFalse(output.contains("Interest applied"));
    }

}
