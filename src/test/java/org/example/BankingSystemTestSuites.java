package org.example;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({MainMenuTests.class,CustomerClassTests.class, CustomerMenuTests.class, EmployeeClassTests.class, EmployeeLoansTests.class, AdminMenuTests.class})
public class BankingSystemTestSuites {
}
