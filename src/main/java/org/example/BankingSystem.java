package org.example;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
// Base class for all accounts
abstract class Account {
    private static final AtomicInteger accountCounter = new AtomicInteger(1);
    private final int accountNumber;
    private double balance;
    private final Customer owner;
    private final List < Transaction > transactions;
    public Account(Customer owner) {
        this.accountNumber = accountCounter.getAndIncrement();
        this.balance = 0.0;
        this.owner = owner;
        this.transactions = new ArrayList < > ();
    }
    public int getAccountNumber() {
        return accountNumber;
    }
    public double getBalance() {
        return balance;
    }
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactions.add(new Transaction("Deposit", amount, balance));
        } else {
            System.out.println("Amount to deposit should be positive.");
        }
    }
    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            transactions.add(new Transaction("Withdraw", amount, balance));
        } else {
            System.out.println("Insufficient balance or invalid amount.");
        }
    }
    public void transfer(Account toAccount, double amount) {
        if (amount > 0 && amount <= balance) {
            this.withdraw(amount);
            toAccount.deposit(amount);
            transactions.add(new Transaction("Transfer", amount, balance, toAccount.getAccountNumber()));
        } else {
            System.out.println("Insufficient balance or invalid amount.");
        }
    }
    public Customer getOwner() {
        return owner;
    }
    public List < Transaction > getTransactions() {
        return transactions;
    }
    public abstract void applyInterest();
    public void printTransactions() {
        System.out.println("Transaction history for account " + accountNumber + ":");
        for (Transaction t: transactions) {
            System.out.println(t);
        }
    }
}
// Savings account with interest
class SavingsAccount extends Account {
    private static final double INTEREST_RATE = 0.03; // 3% annual interest
    public SavingsAccount(Customer owner) {
        super(owner);
    }
    @Override
    public void applyInterest() {
        double interest = getBalance() * INTEREST_RATE;
        deposit(interest);
        System.out.println("Interest applied: " + interest);
    }
}
// Current account with overdraft
class CurrentAccount extends Account {
    private static final double OVERDRAFT_LIMIT = -500.00;
    public CurrentAccount(Customer owner) {
        super(owner);
    }
    @Override
    public void withdraw(double amount) {
        if (amount > 0 && getBalance() - amount >= OVERDRAFT_LIMIT) {
            super.withdraw(amount);
        } else {
            System.out.println("Overdraft limit reached or invalid amount.");
        }
    }
    @Override
    public void applyInterest() {
        System.out.println("No interest on current accounts");
    }
}
// Transaction class to hold transaction details
class Transaction {
    private final String type;
    private final double amount;
    private final double postBalance;
    private final int toAccountNumber;
    public Transaction(String type, double amount, double postBalance) {
        this(type, amount, postBalance, -1);
    }
    public Transaction(String type, double amount, double postBalance, int toAccountNumber) {
        this.type = type;
        this.amount = amount;
        this.postBalance = postBalance;
        this.toAccountNumber = toAccountNumber;
    }
    @Override
    public String toString() {
        if (toAccountNumber != -1) {
            return String.format("%s of %.2f, New balance: %.2f, Transferred to account: %d", type, amount, postBalance, toAccountNumber);
        } else {
            return String.format("%s of %.2f, New balance: %.2f", type, amount, postBalance);
        }
    }
}
// Loan class
class Loan {
    private static final AtomicInteger loanCounter = new AtomicInteger(1656);
    private final int loanId;
    private final Customer customer;
    private double loanAmount;
    private double initialLoanAmount;
    private boolean approved;
    private boolean closed;
    public Loan(Customer customer, double loanAmount) {
        this.loanId = loanCounter.getAndIncrement();
        this.customer = customer;
        this.loanAmount = loanAmount;
        this.initialLoanAmount = loanAmount;
        this.approved = false;
        this.closed = false;
    }
    public int getLoanId() {
        return loanId;
    }
    public Customer getCustomer() {
        return customer;
    }
    public double getLoanAmount() {
        return loanAmount;
    }
    public double getInitialLoanAmount() {
        return initialLoanAmount;
    }
    public boolean isApproved() {
        return approved;
    }
    public boolean isClosed() {
        return closed;
    }
    public void approveLoan() {
        this.approved = true;
        this.customer.getAccounts().get(0).deposit(loanAmount);
    }
    public boolean checkIsEligible(double amount) {
        double currentBalance = customer.getAccounts().get(0).getBalance();
        return currentBalance >= amount;
    }
    public void paidOff(double amount) {
        double currentBalance = customer.getAccounts().get(0).getBalance();
        if (currentBalance > amount) {
            customer.getAccounts().get(0).withdraw(amount);
        }
    }
    public void payOffLoan(double amount) {
        boolean isEligible = checkIsEligible(amount);
        if (!isEligible) {
            System.out.println("You do not have enough balance to pay off this loan currently!");
            System.out.println("Please try a lower amount");
        } else if (amount > 0 && amount <= loanAmount) {
            loanAmount -= amount;
            if (loanAmount == 0) {
                closeLoan();
                System.out.println("You have completely paid off the loan.");
            } else {
                System.out.println("Payment of " + amount + " received. Remaining Loan balance: " + loanAmount);
            }
            paidOff(amount);
        } else {
            System.out.println("Invalid payment amount.");
        }
    }
    public void closeLoan() {
        this.closed = true;
    }
}
// Customer class
class Customer {
    private final String name;
    private final String address;
    private final String phoneNumber;
    private final String username;
    private String password;
    private final List < Account > accounts;
    private final List < Loan > loans;
    public Customer(String name, String address, String phoneNumber, String username, String password) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
        this.accounts = new ArrayList < > ();
        this.loans = new ArrayList < > ();
    }
    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public List < Account > getAccounts() {
        return accounts;
    }
    public void addAccount(Account account) {
        accounts.add(account);
    }
    public void applyForLoan(double amount) {
        Loan loan = new Loan(this, amount);
        loans.add(loan);
        System.out.println("Loan application for " + amount + " submitted.");
        System.out.println("You loan unique id is: " + loan.getLoanId());
    }
    public void payOffLoan(int loanId, double amount) {
        for (Loan loan: loans) {
            if (loan.getLoanId() == loanId) {
                if (!loan.isApproved()) {
                    System.out.println("Loan is not yet approved!");
                    return;
                } else if(loan.getLoanAmount() < amount) {
                    System.out.println("Payoff amount exceeded!");
                    return;
                }
                loan.payOffLoan(amount);
                System.out.println("Loan paid off!");
                return;
            }
        }
        System.out.println("Loan not found.");
    }
    public List < Loan > getLoans() {
        return loans;
    }
    public List < Loan > getExistingLoans() {
        List<Loan> existingLoans = new ArrayList < > ();
        for (Loan loan: loans) {
            if (!loan.isClosed()) {
                existingLoans.add(loan);
            }
        }
        return existingLoans;
    }
    public List < Loan > getOlderLoans() {
        List < Loan > olderLoans = new ArrayList < > ();
        for (Loan loan: loans) {
            if (loan.isClosed()) {
                olderLoans.add(loan);
            }
        }
        return olderLoans;
    }
    public void viewAccount() {
        Account viewAccount = this.getAccounts().get(0);
        if (viewAccount != null) {
            System.out.println("Account Number: " + viewAccount.getAccountNumber());
            System.out.println("Balance: " + viewAccount.getBalance());
            System.out.println("Owner: " + viewAccount.getOwner().getName());
        } else {
            System.out.println("Account not found.");
        }
    }
    public void viewTransactions() {
        Account transactionAccount = this.getAccounts().get(0);
        if (transactionAccount != null) {
            transactionAccount.printTransactions();
        } else {
            System.out.println("Account not found.");
        }
    }
}
//Employee class
class Employee {
    private final String name;
    private final String phoneNumber;
    private final String username;
    private String password;
    private static final AtomicInteger employeeCounter = new AtomicInteger(100);
    private final int employeeNumber;
    public Employee(String name, String phoneNumber, String username, String password) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
        this.employeeNumber = employeeCounter.getAndIncrement();
    }
    public String getName() {
        return name;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getEmployeeNumber() {
        return this.employeeNumber;
    }
}
// Main class
public class BankingSystem {
    private final List < Customer > customers;
    private final List < Employee > employees;
    private final String adminUsername = "admin";
    private final String adminPassword = "admin123";
    public BankingSystem() {
        this.customers = new ArrayList < > ();
        this.employees = new ArrayList < > ();
    }
    public void addCustomer(Customer customer) {
        if(customer==null) return;
        customers.add(customer);
    }
    public List < Customer > getCustomers() {
        return customers;
    }
    public List < Employee > getEmployees() {
        return employees;
    }
    public Customer verify(String name,String address, String phoneNumber,String username,String password){
        if(name.isBlank()){
            System.out.println("Customer name cannot be blank");
            return null;
        }
        if(phoneNumber.isBlank()){
            System.out.println("Customer phone number cannot be blank");
            return null;
        }
        if(username.isBlank()){
            System.out.println("Customer username cannot be blank");
            return null;
        }
        if(this.findCustomerByName(username)!=null){
            System.out.println("Username already exists");
            return null;
        }
        if(password.isBlank()){
            System.out.println("Customer password cannot be blank");
            return null;
        }
        return new Customer(name, address, phoneNumber, username, password);
    }
    public Account verifyAccount(Customer customer, int accountType){
        Account account = null;
        if (accountType == 1) {
            account = new SavingsAccount(customer);
        } else if (accountType == 2) {
            account = new CurrentAccount(customer);
        }
        return account;
    }
    public Employee verifyEmployee(String name, String phoneNumber, String username, String password){
        if(name.isBlank()){
            System.out.println("Employee name cannot be blank");
            return null;
        }
        if(phoneNumber.isBlank()){
            System.out.println("Employee phone number cannot be blank");
            return null;
        }
        if(username.isBlank()){
            System.out.println("Employee username cannot be blank");
            return null;
        }
        if(this.findEmployeeByName(username)!=null){
            System.out.println("Employee already exists");
            return null;
        }
        if(password.isBlank()){
            System.out.println("Employee password cannot be blank");
            return null;
        }
        return new Employee(name, phoneNumber, username, password);
    }
    public void addEmployee(Employee employee) {
        if(employee==null) return;
        employees.add(employee);
    }
    public Customer findCustomerByName(String name) {
        for (Customer customer: customers) {
            if (customer.getName().equalsIgnoreCase(name)) {
                return customer;
            }
        }
        return null;
    }
    public Employee findEmployeeByName(String name) {
        for (Employee employee: employees) {
            if (employee.getName().equalsIgnoreCase(name)) {
                return employee;
            }
        }
        return null;
    }
    public Account findAccountByNumber(int accountNumber) {
        for (Customer customer: customers) {
            for (Account account: customer.getAccounts()) {
                if (account.getAccountNumber() == accountNumber) {
                    return account;
                }
            }
        }
        return null;
    }
    public Loan findLoanByLoanNumber(int loanNumber) {
        for (Customer customer: customers) {
            for (Loan loan: customer.getLoans()) {
                if (loan.getLoanId() == loanNumber) {
                    return loan;
                }
            }
        }
        return null;
    }
    public Customer authenticateUser(String username, String password) {
        for (Customer customer: customers) {
            if (customer.getUsername().equals(username) && customer.getPassword().equals(password)) {
                return customer;
            }
        }
        return null;
    }
    public Employee authenticateEmployee(String username, String password) {
        for (Employee employee: employees) {
            if (employee.getUsername().equals(username) && employee.getPassword().equals(password)) {
                return employee;
            }
        }
        return null;
    }
    public boolean resetUserPassword(String username, String currentPassword, String newPassword) {
        Customer customer = authenticateUser(username, currentPassword);
        if (customer != null) {
            customer.setPassword(newPassword);
            System.out.println("Password changed successfully.");
            return true;
        } else {
            System.out.println("Invalid current password.");
            return false;
        }
    }
    public boolean resetEmployeePassword(Employee employee, String currentPassword, String newPassword) {
        Employee correctEmployee = authenticateEmployee(employee.getUsername(), currentPassword);
        if (correctEmployee != null) {
            correctEmployee.setPassword(newPassword);
            System.out.println("Password changed successfully.");
            return true;
        }
        System.out.println("Invalid current password.");
        return false;
    }
    public List < Loan > getUnapprovedLoans() {
        List < Loan > unapprovedLoans = new ArrayList < > ();
        for (Customer customer: customers) {
            for (Loan loan: customer.getLoans()) {
                if (!loan.isApproved() && !loan.isClosed()) {
                    unapprovedLoans.add(loan);
                }
            }
        }
        return unapprovedLoans;
    }
    public List < Loan > getApprovedLoans() {
        List < Loan > approvedLoans = new ArrayList < > ();
        for (Customer customer: customers) {
            for (Loan loan: customer.getLoans()) {
                if (loan.isApproved() && !loan.isClosed()) {
                    approvedLoans.add(loan);
                }
            }
        }
        return approvedLoans;
    }
    public List < Loan > getClosedLoans() {
        List < Loan > closedLoans = new ArrayList < > ();
        for (Customer customer: customers) {
            for (Loan loan: customer.getLoans()) {
                if (loan.isClosed()) {
                    closedLoans.add(loan);
                }
            }
        }
        return closedLoans;
    }
    public boolean authenticateAdmin(String username, String password) {
        return adminUsername.equals(username) && adminPassword.equals(password);
    }
    public static void initializeData(BankingSystem bankingSystem) {
        String[] name = {
                "Aarav",
                "Ananya",
                "Rohan",
                "Sneha",
                "Arjun",
                "Meera",
                "Vivek",
                "Neha",
                "Manan",
                "Yash"
        };
        String[] addr = {
                "123 Main St, Apt 4B",
                "456 Oak Dr, Unit 12",
                "789 Pine Ln, Suite 300",
                "101 Maple Ave",
                "202 Birch Rd, Floor 2",
                "303 Cedar Blvd",
                "404 Spruce St",
                "505 Elm Ct",
                "606 Willow Way, Apt 7",
                "707 Ash Pl, Unit 5"
        };
        String[] phone = {
                "9876543210",
                "8765432109",
                "7654321098",
                "6543210987",
                "5432109876",
                "4321098765",
                "3210987654",
                "2109876543",
                "1098765432",
                "0987654321"
        };
        String[] username = {
                "aarav",
                "ananya",
                "rohan",
                "sneha",
                "arjun",
                "meera",
                "vivek",
                "neha",
                "manan",
                "yash"
        };
        String[] password = {
                "aarav",
                "ananya",
                "rohan",
                "sneha",
                "arjun",
                "meera",
                "vivek",
                "neha",
                "manan",
                "yash"
        };
        double[] loanAmount = {
                1000,
                2000,
                3000,
                4000,
                5000,
                6000,
                7000,
                8000,
                9000,
                10000
        };
        for (int i = 0; i < 10; i++) {
            Customer customer =
                    new Customer(name[i], addr[i], phone[i], username[i], password[i]);
            bankingSystem.addCustomer(customer);
            Account account = new SavingsAccount(customer);
            customer.addAccount(account);
            customer.applyForLoan(loanAmount[i]);
            if (i % 2 == 0) {
                customer.getLoans().get(0).approveLoan();
            }
        }
        for (int i = 0; i < 10; i++) {
            Employee employee =
                    new Employee(name[i], phone[i], username[i], password[i]);
            bankingSystem.addEmployee(employee);
        }
    }
    public void loginUser(BankingSystem bankingSystem, Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        Customer authenticatedCustomer = bankingSystem.authenticateUser(username, password);
        if (authenticatedCustomer != null) {
            System.out.println("Login successful. Welcome, " + authenticatedCustomer.getName() + "!");
            customerMenu(bankingSystem, authenticatedCustomer, scanner);
        } else {
            System.out.println("Invalid username or password.");
        }
    }
    public void register(BankingSystem bankingSystem, Scanner scanner) {
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();
        System.out.print("Enter customer address: ");
        String address = scanner.nextLine();
        System.out.print("Enter customer phone number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        Customer customer = bankingSystem.verify(name,address,phoneNumber,username,password);
        if (customer == null) {
            return;
        }
        System.out.println("Choose account type:");
        System.out.println("1. Savings Account");
        System.out.println("2. Current Account");
        System.out.print("Choose an option: ");
        int accountType = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        Account account = bankingSystem.verifyAccount(customer,accountType);
        if (account != null) {
            customer.addAccount(account);
            System.out.println("Account added for customer " + customer.getName());
        }else{
            System.out.println("Invalid account type.");
            return;
        }
        bankingSystem.addCustomer(customer);
        System.out.println("Customer added.");
        System.out.println("Their account number is: " + customer.getAccounts().get(0).getAccountNumber());
    }
    public void mainMenu(BankingSystem bankingSystem, Scanner scanner) {
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("Banking System Menu:");
            System.out.println("1. Register");
            System.out.println("2. Login as user");
            System.out.println("3. Login as employee");
            System.out.println("4. Login as admin");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    register(bankingSystem, scanner);
                    break;
                case 2:
                    loginUser(bankingSystem, scanner);
                    break;
                case 3:
                    // Employee case
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    Employee authenticatedEmployee = bankingSystem.authenticateEmployee(username, password);
                    if (authenticatedEmployee != null) {
                        System.out.println("Login successful. Welcome, " + authenticatedEmployee.getName() + "!");
                        employeeMenu(bankingSystem, authenticatedEmployee, scanner);
                    } else {
                        System.out.println("Invalid username or password.");
                    }
                    break;
                case 4:
                    System.out.print("Enter admin username: ");
                    username = scanner.nextLine();
                    System.out.print("Enter admin password: ");
                    password = scanner.nextLine();
                    if (bankingSystem.authenticateAdmin(username, password)) {
                        System.out.println("Admin login successful.");
                        adminMenu(bankingSystem, scanner);
                    } else {
                        System.out.println("Invalid admin credentials.");
                    }
                    break;
                case 5:
                    System.out.println("Exiting system.");
                    scanner.close();
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    public static void main(String[] args) {
        BankingSystem bankingSystem = new BankingSystem();
        initializeData(bankingSystem);
        Scanner scanner = new Scanner(System.in);
        bankingSystem.mainMenu(bankingSystem, scanner);
    }
    public void depositMoney(BankingSystem bankingSystem, int accountNumber, Scanner scanner) {
        System.out.print("Enter deposit amount: ");
        double depositAmount = scanner.nextDouble();
        Account depositAccount = bankingSystem.findAccountByNumber(accountNumber);
        if (depositAccount != null) {
            depositAccount.deposit(depositAmount);
            System.out.println("Amount deposited.");
        } else {
            System.out.println("Account not found.");
        }
    }
    public void withdrawMoney(BankingSystem bankingSystem, int accountNumber, Scanner scanner) {
        System.out.print("Enter withdrawal amount: ");
        double withdrawalAmount = scanner.nextDouble();
        Account withdrawAccount = bankingSystem.findAccountByNumber(accountNumber);
        if (withdrawAccount != null) {
            withdrawAccount.withdraw(withdrawalAmount);
            System.out.println("Amount withdrawn.");
        } else {
            System.out.println("Account not found.");
        }
    }
    public void transferMoney(BankingSystem bankingSystem, int sourceAccountNumber, Scanner scanner) {
        System.out.print("Enter destination account number: ");
        int destinationAccountNumber = scanner.nextInt();
        System.out.print("Enter transfer amount: ");
        double transferAmount = scanner.nextDouble();
        Account sourceAccount = bankingSystem.findAccountByNumber(sourceAccountNumber);
        Account destinationAccount = bankingSystem.findAccountByNumber(destinationAccountNumber);
        if (sourceAccount != null && destinationAccount != null) {
            sourceAccount.transfer(destinationAccount, transferAmount);
            System.out.println("Amount transferred.");
        } else {
            System.out.println("Source or destination account not found.");
        }
    }
    public void customerMenu(BankingSystem bankingSystem, Customer customer, Scanner scanner) {
        int accountNumber = customer.getAccounts().get(0).getAccountNumber();
        while (true) {
            System.out.println("Customer Menu:");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Transfer");
            System.out.println("4. View Account");
            System.out.println("5. View Transaction History");
            System.out.println("6. Loans");
            System.out.println("7. Reset my password");
            System.out.println("8. Logout");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            switch (option) {
                case 1:
                    depositMoney(bankingSystem, accountNumber, scanner);
                    break;
                case 2:
                    withdrawMoney(bankingSystem, accountNumber, scanner);
                    break;
                case 3:
                    transferMoney(bankingSystem, accountNumber, scanner);
                    break;
                case 4:
                    customer.viewAccount();
                    break;
                case 5:
                    customer.viewTransactions();
                    break;
                case 6:
                    loanSection(bankingSystem, customer, scanner);
                    break;
                case 7:
                    System.out.print("Enter current password: ");
                    String currentPassword = scanner.nextLine();
                    System.out.print("Enter new password: ");
                    String newPassword = scanner.nextLine();
                    bankingSystem.resetUserPassword(customer.getUsername(), currentPassword, newPassword);
                    break;
                case 8:
                    System.out.println("Logging out.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    public void adminMenu(BankingSystem bankingSystem, Scanner scanner) {
        while (true) {
            System.out.println("Admin Menu:");
            System.out.println("1. Create a new user Account");
            System.out.println("2. Create a new Employee");
            System.out.println("3. View Account");
            System.out.println("4. View Transaction History");
            System.out.println("5. Apply Interest");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            switch (option) {
                case 1:
                    register(bankingSystem, scanner);
                    break;
                case 2:
                    // Add new Employee
                    System.out.print("Enter employee name: ");
                    String empName = scanner.nextLine();
                    System.out.print("Enter employee phone number: ");
                    String empPhoneNumber = scanner.nextLine();
                    System.out.print("Enter username: ");
                    String empUsername = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String empPassword = scanner.nextLine();
                    Employee employee = bankingSystem.verifyEmployee(empName, empPhoneNumber, empUsername, empPassword);
                    if (employee == null) {
                        break;
                    }
                    bankingSystem.addEmployee(employee);
                    System.out.println("Employee created.");
                    System.out.println("Their employee number is: " + employee.getEmployeeNumber());
                    break;
                case 3:
                    System.out.print("Enter account number: ");
                    int accountNumber = scanner.nextInt();
                    Account viewAccount = bankingSystem.findAccountByNumber(accountNumber);
                    if (viewAccount != null) {
                        System.out.println("Account Number: " + viewAccount.getAccountNumber());
                        System.out.println("Balance: " + viewAccount.getBalance());
                        System.out.println("Owner: " + viewAccount.getOwner().getName());
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;
                case 4:
                    System.out.print("Enter account number: ");
                    accountNumber = scanner.nextInt();
                    Account transactionAccount = bankingSystem.findAccountByNumber(accountNumber);
                    if (transactionAccount != null) {
                        transactionAccount.printTransactions();
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;
                case 5:
                    System.out.print("Enter account number: ");
                    accountNumber = scanner.nextInt();
                    Account interestAccount = bankingSystem.findAccountByNumber(accountNumber);
                    if (interestAccount != null) {
                        interestAccount.applyInterest();
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;
                case 6:
                    System.out.println("Logging out.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    public void applyInterest(BankingSystem bankingSystem, Scanner scanner) {
        System.out.print("Enter account number: ");
        int accountNumber = scanner.nextInt();
        Account interestAccount = bankingSystem.findAccountByNumber(accountNumber);
        if (interestAccount != null) {
            interestAccount.applyInterest();
            System.out.println("Interest applied");
        } else {
            System.out.println("Account not found.");
        }
    }
    public void employeeMenu(BankingSystem bankingSystem, Employee employee, Scanner scanner) {
        while (true) {
            System.out.println("Employee Menu:");
            System.out.println("1. Create a new user Account");
            System.out.println("2. View Account");
            System.out.println("3. View Transaction History");
            System.out.println("4. Apply Interest");
            System.out.println("5. Loans");
            System.out.println("6. Change password");
            System.out.println("7. Logout");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            switch (option) {
                case 1:
                    System.out.print("Enter customer name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter customer address: ");
                    String address = scanner.nextLine();
                    System.out.print("Enter customer phone number: ");
                    String phoneNumber = scanner.nextLine();
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    Customer customer = new Customer(name, address, phoneNumber, username, password);
                    bankingSystem.addCustomer(customer);
                    System.out.println("Choose account type:");
                    System.out.println("1. Savings Account");
                    System.out.println("2. Current Account");
                    System.out.print("Choose an option: ");
                    int accountType = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    Account account = null;
                    if (accountType == 1) {
                        account = new SavingsAccount(customer);
                    } else if (accountType == 2) {
                        account = new CurrentAccount(customer);
                    } else {
                        System.out.println("Invalid account type.");
                    }
                    if (account != null) {
                        customer.addAccount(account);
                        System.out.println("Account added for customer " + customer.getName());
                    }
                    System.out.println("Customer added.");
                    System.out.println("Their account number is: " + customer.getAccounts().get(0).getAccountNumber());
                    break;
                case 2:
                    System.out.print("Enter account number: ");
                    int accountNumber = scanner.nextInt();
                    Account viewAccount = bankingSystem.findAccountByNumber(accountNumber);
                    if (viewAccount != null) {
                        System.out.println("Account Number: " + viewAccount.getAccountNumber());
                        System.out.println("Balance: " + viewAccount.getBalance());
                        System.out.println("Owner: " + viewAccount.getOwner().getName());
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;
                case 3:
                    System.out.print("Enter account number: ");
                    accountNumber = scanner.nextInt();
                    Account transactionAccount = bankingSystem.findAccountByNumber(accountNumber);
                    if (transactionAccount != null) {
                        transactionAccount.printTransactions();
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;
                case 4:
                    applyInterest(bankingSystem, scanner);
                    break;
                case 5:
                    employeeLoanSection(bankingSystem, scanner);
                    break;
                case 6:
                    System.out.print("Enter current password: ");
                    String currentPassword = scanner.nextLine();
                    System.out.print("Enter new password: ");
                    String newPassword = scanner.nextLine();
                    bankingSystem.resetEmployeePassword(employee, currentPassword, newPassword);
                    break;
                case 7:
                    System.out.println("Logging out.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    private static void loanSection(BankingSystem bankingSystem, Customer customer, Scanner scanner) {
        while (true) {
            System.out.println("Loan Section");
            System.out.println("1. Apply for a new Loan");
            System.out.println("2. View Existing loans");
            System.out.println("3. View Older loans");
            System.out.println("4. Payoff an existing loan");
            System.out.println("5. Back");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            switch (option) {
                case 1:
                    System.out.print("Enter amount: ");
                    double amount = scanner.nextDouble();
                    customer.applyForLoan(amount);
                    break;
                case 2:
                    List < Loan > loans = customer.getExistingLoans();
                    if (loans.size() == 0) {
                        System.out.println("You do not have any current loans.");
                        break;
                    } else {
                        int size = loans.size();
                        for (int i = 0; i < size; i++) {
                            Loan loan = loans.get(i);
                            int loanNumber = loan.getLoanId();
                            double loanAmount = loan.getLoanAmount();
                            boolean isApproved = loan.isApproved();
                            if (isApproved) {
                                System.out.println("Approved Loan Number: " + loanNumber + " \tAmount:" + loanAmount);
                            } else {
                                System.out.println("Pending Loan Number: " + loanNumber + " \tAmount:" + loanAmount);
                            }
                        }
                    }
                    break;
                case 3:
                    List < Loan > olderLoans = customer.getOlderLoans();
                    if (olderLoans.size() == 0) {
                        System.out.println("You do not have any finised loans!");
                        break;
                    }
                    for (Loan loan: olderLoans) {
                        System.out.println("Loan Number: " + loan.getLoanId() + " \tOrignal Loan Amount: " + loan.getInitialLoanAmount());
                    }
                    break;
                case 4:
                    System.out.print("Choose Loan Number: ");
                    int loanNumber = scanner.nextInt();
                    System.out.print("Enter amount to payoff: ");
                    double payOffAmount = scanner.nextDouble();
                    customer.payOffLoan(loanNumber, payOffAmount);
                    break;
                case 5:
                    System.out.println("Going back");
                    return;
                default:
                    break;
            }
        }
    }
    private static void employeeLoanSection(BankingSystem bankingSystem, Scanner scanner) {
        while (true) {
            System.out.println("Loan Section");
            System.out.println("1. Approve Loans");
            System.out.println("2. View Existing loans status");
            System.out.println("3. View Closed loans");
            System.out.println("4. Back");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            switch (option) {
                case 1:
                    List < Loan > unapprovedLoans = bankingSystem.getUnapprovedLoans();
                    for (Loan loan: unapprovedLoans) {
                        double loanAmount = loan.getLoanAmount();
                        int loanNumber = loan.getLoanId();
                        String customerName = loan.getCustomer().getName();
                        System.out.println("Loan Number: " + loanNumber + " \tLoan Amount: " + loanAmount + " \tCustomer Name: " + customerName);
                    }
                    System.out.print("Choose loan number to approve: ");
                    int approvedLoanNumer = scanner.nextInt();
                    Loan loanToBeApproved = bankingSystem.findLoanByLoanNumber(approvedLoanNumer);
                    if (loanToBeApproved != null) {
                        loanToBeApproved.approveLoan();
                        System.out.println("Loan has been approved");
                    } else {
                        System.out.println("Incorrect Loan number!");
                    }
                    break;
                case 2:
                    List < Loan > approvedLoans = bankingSystem.getApprovedLoans();
                    System.out.println("Here are the details for all the current loans");
                    for (Loan loan: approvedLoans) {
                        double loanAmount = loan.getLoanAmount();
                        int loanNumber = loan.getLoanId();
                        String customerName = loan.getCustomer().getName();
                        System.out.println("Loan Number: " + loanNumber + " \tLoan Amount: " + loanAmount + " \tCustomer Name: " + customerName);
                    }
                    break;
                case 3:
                    List < Loan > closedLoans = bankingSystem.getClosedLoans();
                    System.out.println("Here are the details for all the current loans");
                    for (Loan loan: closedLoans) {
                        double loanAmount = loan.getLoanAmount();
                        int loanNumber = loan.getLoanId();
                        String customerName = loan.getCustomer().getName();
                        System.out.println("Loan Number: " + loanNumber + " \tLoan Amount: " + loanAmount + " \tCustomer Name: " + customerName);
                    }
                    break;
                case 4:
                    return;
                default:
                    break;
            }
        }
    }
}