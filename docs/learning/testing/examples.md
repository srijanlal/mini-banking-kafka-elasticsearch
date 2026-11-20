# JUnit 5 and Mockito Sample Code

The goal of this sample is to show the main interview topics in one place:

- test lifecycle hooks
- assertions
- exception testing
- parameterized tests
- timeouts
- test suites
- mocks, spies, and argument capture
- void methods and exception stubbing
- static method mocking
- constructor injection

## 1. Pure JUnit example

This example covers `@BeforeEach`, `@Test`, `assertEquals`, `assertTrue`, `assertSame`, `assertThrows`, parameterized tests, and timeouts.

```java
package com.example.learning.testing;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Calculator {

    int add(int left, int right) {
        return left + right;
    }

    int divide(int left, int right) {
        if (right == 0) {
            throw new IllegalArgumentException("divisor must not be zero");
        }
        return left / right;
    }

    String identity(String value) {
        return value;
    }
}

class CalculatorTest {

    private Calculator calculator;
    private String sharedReference;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
        sharedReference = "shared";
    }

    @Test
    void add_returnsExpectedSum() {
        // Arrange
        int left = 2;
        int right = 3;

        // Act
        int result = calculator.add(left, right);

        // Assert
        assertEquals(5, result);
        assertTrue(result > 0);
    }

    @Test
    void identity_returnsSameObjectReference() {
        String returned = calculator.identity(sharedReference);

        assertSame(sharedReference, returned);
    }

    @Test
    void divide_throwsForZeroDivisor() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calculator.divide(10, 0));

        assertEquals("divisor must not be zero", exception.getMessage());
    }

    @ParameterizedTest(name = "{0} + {1} = {2}")
    @CsvSource({
            "1, 2, 3",
            "10, 20, 30",
            "-5, 5, 0"
    })
    void add_worksForMultipleInputs(int left, int right, int expected) {
        assertEquals(expected, calculator.add(left, right));
    }

    @Test
    void slowOperation_completesWithinTimeout() {
        assertTimeout(Duration.ofMillis(100), () -> {
            Thread.sleep(10);
            return null;
        });
    }
}
```

What this shows:

- `@BeforeEach` creates a fresh test fixture for each test method.
- `assertEquals` checks value equality.
- `assertTrue` checks a boolean condition.
- `assertSame` checks that both references point to the same object.
- `assertThrows` is the preferred way to test expected exceptions in JUnit 5.
- `@ParameterizedTest` prevents copy-paste tests.
- `assertTimeout` keeps slow code visible without making the test brittle.

## 2. Mockito example with constructor injection

This example covers `@Mock`, `@InjectMocks`, `when(...).thenReturn(...)`, `verify(...)`, void method stubbing, `doThrow(...)`, and `ArgumentCaptor`.

```java
package com.example.learning.testing;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

record Account(String id, String name, String email) {
}

interface AccountRepository {
    Optional<Account> findById(String id);
}

interface NotificationClient {
    void sendWelcomeEmail(String email);
}

class AccountNotFoundException extends RuntimeException {
    AccountNotFoundException(String message) {
        super(message);
    }
}

class AccountService {

    private final AccountRepository accountRepository;
    private final NotificationClient notificationClient;

    AccountService(AccountRepository accountRepository, NotificationClient notificationClient) {
        this.accountRepository = accountRepository;
        this.notificationClient = notificationClient;
    }

    String getGreeting(String accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));

        notificationClient.sendWelcomeEmail(account.email());
        return "Hello, " + account.name();
    }
}

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private NotificationClient notificationClient;

    @InjectMocks
    private AccountService accountService;

    @Test
    void getGreeting_returnsGreetingAndSendsEmail() {
        // Arrange
        Account account = new Account("A1", "Alice", "alice@example.com");
        when(accountRepository.findById("A1")).thenReturn(Optional.of(account));

        // Act
        String greeting = accountService.getGreeting("A1");

        // Assert
        assertEquals("Hello, Alice", greeting);

        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        verify(notificationClient).sendWelcomeEmail(emailCaptor.capture());
        assertEquals("alice@example.com", emailCaptor.getValue());
    }

    @Test
    void getGreeting_throwsWhenAccountDoesNotExist() {
        when(accountRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.getGreeting("missing"));
    }

    @Test
    void voidMethodCanThrowWithDoThrow() {
        Account account = new Account("A1", "Alice", "alice@example.com");
        when(accountRepository.findById("A1")).thenReturn(Optional.of(account));
        doThrow(new IllegalStateException("smtp down")).when(notificationClient).sendWelcomeEmail(anyString());

        assertThrows(IllegalStateException.class, () -> accountService.getGreeting("A1"));
    }

    @Test
    void verifyCanCheckInteractionCount() {
        Account account = new Account("A1", "Alice", "alice@example.com");
        when(accountRepository.findById("A1")).thenReturn(Optional.of(account));

        accountService.getGreeting("A1");
        accountService.getGreeting("A1");

        verify(accountRepository, times(2)).findById("A1");
    }
}
```

What this shows:

- `@Mock` replaces real dependencies.
- `@InjectMocks` builds the class under test and injects the mocks.
- `when(...).thenReturn(...)` controls the dependency response.
- `verify(...)` checks behavior.
- `ArgumentCaptor` lets you inspect the exact value passed to a dependency.
- `doThrow(...)` is the right style for void methods.

## 3. Static methods, spies, and deep stubs

This last section shows the tricky interview topics.

```java
package com.example.learning.testing;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

final class TaxCalculator {

    private TaxCalculator() {
    }

    static int calculateTax(int amount) {
        return amount / 10;
    }
}

class PricingService {

    int finalPrice(int amount) {
        return amount + TaxCalculator.calculateTax(amount);
    }
}

class PricingServiceTest {

    @Test
    void staticMethodCanBeMockedWithMockedStatic() {
        PricingService pricingService = new PricingService();

        try (MockedStatic<TaxCalculator> mocked = mockStatic(TaxCalculator.class)) {
            mocked.when(() -> TaxCalculator.calculateTax(100)).thenReturn(18);

            assertEquals(118, pricingService.finalPrice(100));
        }
    }

    @Test
    void spyKeepsRealBehaviorUnlessOverridden() {
        List<String> list = spy(new ArrayList<>());

        list.add("real-value");

        doReturn("stubbed").when(list).get(0);

        assertEquals("stubbed", list.get(0));
    }

    @Test
    void deepStubsCanHandleChainedCallsButShouldBeUsedCarefully() {
        OrderRepository orderRepository = mock(OrderRepository.class, RETURNS_DEEP_STUBS);
        when(orderRepository.currentOrder().customer().name()).thenReturn("Riya");

        assertEquals("Riya", orderRepository.currentOrder().customer().name());
    }
}

interface OrderRepository {
    Order currentOrder();
}

interface Order {
    Customer customer();
}

interface Customer {
    String name();
}
```

What this shows:

- Static method mocking needs Mockito inline support.
- `spy()` wraps a real object and overrides only what you stub.
- `RETURNS_DEEP_STUBS` is a convenience for chained calls, but it usually means the design is too nested.

## 4. Test suite example

JUnit 5 suites are a separate runner layer, so they are often configured in a dedicated suite class.

```java
package com.example.learning.testing;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({CalculatorTest.class, AccountServiceTest.class, PricingServiceTest.class})
class AllTestsSuite {
}
```

## 5. Practical interview summary

- Use JUnit 5 for test structure and assertions.
- Use Mockito to isolate dependencies.
- Test private methods indirectly.
- Prefer `assertThrows` over legacy exception annotations.
- Prefer constructor injection in production code because it makes tests simpler.
- Avoid deep stubs and static mocks unless a refactor is not practical.
- Keep tests deterministic, small, and readable.