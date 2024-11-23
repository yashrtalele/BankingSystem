package org.example;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class MainMenuTests {
    private BankingSystem bankingSystem;
    private Customer customer;
    private Employee employee;

    @Before
    public void setUp() {
        bankingSystem = new BankingSystem();
        customer = new Customer("jay","india","1234," ,"jay" ,"jay");
        bankingSystem.addCustomer(customer);
        employee = new Employee("som","5678", "som", "som");
        bankingSystem.addEmployee(employee);
    }

    @Test
    public void invalidCustomerLogin() {
        Assertions.assertNull(bankingSystem.authenticateUser("jo", "jo"));
    }

    @Test
    public void validCustomerLogin() {
        Assertions.assertEquals(customer, bankingSystem.authenticateUser("jay", "jay"));
    }

    @Test
    public void invalidEmployeeLogin() {
        Assertions.assertNull(bankingSystem.authenticateEmployee("som", "so"));
    }

    @Test
    public void validEmployeeLogin() {
        Assertions.assertEquals(employee, bankingSystem.authenticateEmployee("som", "som"));
    }

    @Test
    public void invalidAdminLogin() {
        Assertions.assertFalse(bankingSystem.authenticateAdmin("admin", "jo"));
    }

    @Test
    public void validAdminLogin() {
        Assertions.assertTrue(bankingSystem.authenticateAdmin("admin", "admin123"));
    }
}