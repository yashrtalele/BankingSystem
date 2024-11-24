package org.example;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Scanner;

public class MainMenuTests {
    private BankingSystem bankingSystem;
    private Customer customer;
    private Employee employee;
    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        bankingSystem = new BankingSystem();
        customer = new Customer("jay","india","1234," ,"jay" ,"jay");
        bankingSystem.addCustomer(customer);
        employee = new Employee("som","5678", "som", "som");
        bankingSystem.addEmployee(employee);
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void testDUPath() {
        Scanner mockScanner = mock(Scanner.class);
        BankingSystem mockBankingSystem = spy(new BankingSystem());
        Customer mockCustomer = mock(Customer.class);
        Employee mockEmployee = mock(Employee.class);
        SavingsAccount mockSavingsAccount = mock(SavingsAccount.class);
        Account mockAccount = mock(Account.class);
        Account mockDestinationAccount = mock(Account.class);
        when(mockBankingSystem.findAccountByNumber(eq(12345)))
                .thenReturn(mockAccount);
        when(mockBankingSystem.findAccountByNumber(eq(9756)))
                .thenReturn(mockDestinationAccount);

        doNothing().when(mockAccount).deposit(anyDouble());
        doNothing().when(mockAccount).withdraw(anyDouble());
        doNothing().when(mockDestinationAccount).deposit(anyDouble());

        when(mockCustomer.getAccounts()).thenReturn(Collections.singletonList(mockSavingsAccount));

        when(mockBankingSystem.authenticateUser(eq("yash"), eq("yash"))).thenReturn(mockCustomer);
        when(mockCustomer.getName()).thenReturn("Yash");

        when(mockBankingSystem.authenticateEmployee(eq("yash"), eq("yash"))).thenReturn(mockEmployee);
        when(mockEmployee.getName()).thenReturn("Yash");

        when(mockScanner.nextInt())
                .thenReturn(2, 1, 2, 3, 9756, 4, 8, 3, 4, 12345, 7, 5);

        when(mockScanner.nextLine())
                .thenReturn("yash", "yash", "yash", "yash");

        when(mockScanner.nextDouble()).thenReturn(10000.0).thenReturn(500.0).thenReturn(1000.0);
        mockBankingSystem.mainMenu(mockBankingSystem, mockScanner);
        verify(mockBankingSystem).authenticateUser("yash", "yash");
        verify(mockBankingSystem).customerMenu(eq(mockBankingSystem), eq(mockCustomer), eq(mockScanner));
        mockBankingSystem.depositMoney(mockBankingSystem, 12345, mockScanner);
        verify(mockAccount, times(1)).deposit(anyDouble());
        mockBankingSystem.withdrawMoney(mockBankingSystem, 12345, mockScanner);
        verify(mockAccount, times(1)).withdraw(anyDouble());
        mockBankingSystem.transferMoney(mockBankingSystem, 12345, mockScanner);
        verify(mockScanner, atLeastOnce()).nextInt();
        verify(mockBankingSystem).authenticateEmployee("yash", "yash");
        verify(mockBankingSystem).employeeMenu(eq(mockBankingSystem), eq(mockEmployee), eq(mockScanner));
        verify(mockBankingSystem).applyInterest(mockBankingSystem, mockScanner);
        verify(mockScanner, atLeastOnce()).nextInt();
        verify(mockScanner, atLeastOnce()).nextInt();
        String output = outputStream.toString();
        Assertions.assertTrue(output.contains("Login successful"), "Login message not found");
        Assertions.assertTrue(output.contains("Amount deposited"), "Deposit message not found");
        Assertions.assertTrue(output.contains("Amount withdrawn"), "Withdraw message not found");
        Assertions.assertTrue(output.contains("Login successful. Welcome, Yash"), "Employee Login message not found");
        Assertions.assertTrue(output.contains("Interest applied"), "Interest applied message not found");
    }

    @Test
    public void testAdminLogin() {
        Scanner mockScanner = mock(Scanner.class);
        BankingSystem mockBankingSystem = mock(BankingSystem.class);

        String username = "admin";
        String password = "admin123";
        when(mockScanner.nextLine()).thenReturn(username).thenReturn(password);
        when(mockBankingSystem.authenticateAdmin(username, password)).thenReturn(true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        System.out.print("Enter admin username: ");
        username = mockScanner.nextLine();
        System.out.print("Enter admin password: ");
        password = mockScanner.nextLine();

        if (mockBankingSystem.authenticateAdmin(username, password)) {
            System.out.println("Admin login successful.");
            mockBankingSystem.adminMenu(mockBankingSystem, mockScanner);
        } else {
            System.out.println("Invalid admin credentials.");
        }

        verify(mockBankingSystem).authenticateAdmin(username, password);

        String output = outputStream.toString();
        Assertions.assertTrue(output.contains("Admin login successful."), "Admin login message not found");
    }

    @Test
    public void testTransferMoney() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Scanner mockScanner = mock(Scanner.class);
            BankingSystem mockBankingSystem = spy(new BankingSystem());
            Customer mockCustomer = mock(Customer.class);
            SavingsAccount mockSavingsAccount = mock(SavingsAccount.class);
            Account mockSourceAccount = mock(Account.class);
            Account mockDestinationAccount = mock(Account.class);

            when(mockBankingSystem.findAccountByNumber(eq(12345))).thenReturn(mockSourceAccount);
            when(mockBankingSystem.findAccountByNumber(eq(9756))).thenReturn(mockDestinationAccount);
            when(mockScanner.nextInt()).thenReturn(9756);
            when(mockScanner.nextDouble()).thenReturn(1000.0);

            doNothing().when(mockSourceAccount).deposit(anyDouble());
            doNothing().when(mockSourceAccount).withdraw(anyDouble());
            doNothing().when(mockDestinationAccount).deposit(anyDouble());

            when(mockSourceAccount.getBalance()).thenReturn(5000.0);
            when(mockDestinationAccount.getBalance()).thenReturn(1000.0);

            mockBankingSystem.transferMoney(mockBankingSystem, 12345, mockScanner);
            verify(mockSourceAccount, times(1)).transfer(eq(mockDestinationAccount), eq(1000.0));

            verify(mockScanner, atLeastOnce()).nextInt();

            String output = outputStream.toString();
            Assertions.assertTrue(output.contains("Amount transferred."), "Transfer message not found");

        } finally {
            System.setOut(originalOut);
        }
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