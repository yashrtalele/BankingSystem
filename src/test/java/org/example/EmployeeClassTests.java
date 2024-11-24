package org.example;


import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class EmployeeClassTests {

    private Employee employee;

    @Before
    public  void setUp() {
        BankingSystem bankingSystem = new BankingSystem();
        employee = new Employee("manan","1234","manan1","manan");
        bankingSystem.addEmployee(employee);
    }

    @Test
    public void validName(){
        Assertions.assertEquals("manan",employee.getName());
    }

    @Test
    public void invalidName(){
        Assertions.assertNotEquals("jay",employee.getName());
    }

    @Test
    public void validPhone(){
        Assertions.assertEquals("1234",employee.getPhoneNumber());
    }

    @Test
    public void invalidPhone(){
        Assertions.assertNotEquals("123",employee.getPhoneNumber());
    }

    @Test
    public void validUsername(){
        Assertions.assertEquals("manan1",employee.getUsername());
    }

    @Test
    public void invalidUsername(){
        Assertions.assertNotEquals("jay",employee.getUsername());
    }

    @Test
    public void invalidEmployeeNumber(){
        Assertions.assertNotEquals(1000,employee.getEmployeeNumber());
    }

    @Test
    public void validPassword(){
        Assertions.assertEquals("manan",employee.getPassword());
    }

    @Test
    public void invalidPassword(){
        Assertions.assertNotEquals("manan1",employee.getPassword());
    }

    @Test
    public void validChangePassword(){
        employee.setPassword("jay");
        Assertions.assertEquals("jay",employee.getPassword());
    }

    @Test
    public void invalidChangePassword(){
        employee.setPassword("jay");
        Assertions.assertNotEquals("manan",employee.getPassword());
    }

}
