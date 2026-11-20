# Testing Interview Notes

This folder is aligned with the test stack used in this repo: Spring Boot 3.x, JUnit 5, Mockito, and AssertJ.

If you studied JUnit 4 interview answers before, keep this rule in mind:

- `@Before` becomes `@BeforeEach`
- `@BeforeClass` becomes `@BeforeAll`
- `@RunWith` is replaced by `@ExtendWith` or JUnit Platform suites
- `@Rule` is replaced by JUnit 5 extensions and `@RegisterExtension`

## JUnit Questions

1. What is JUnit, and why is it important for unit testing?

JUnit is the standard Java testing framework used to write and run automated tests. It gives you annotations like `@Test`, lifecycle hooks, and assertions so you can verify behavior in a repeatable way. It matters because it helps catch bugs early, supports test-driven development, and protects existing behavior during refactoring.

2. Explain the difference between `@Before` and `@BeforeClass`. How are they used?

In JUnit 4, `@Before` runs before every test method, while `@BeforeClass` runs once before the whole class. In JUnit 5, the equivalents are `@BeforeEach` and `@BeforeAll`. Use per-test setup for fresh objects and per-class setup for expensive shared initialization.

3. How do you test expected exceptions in JUnit?

In JUnit 5, use `assertThrows(SomeException.class, () -> ...)`. This is better than the old JUnit 4 `expected` attribute because you can inspect the exception message and keep the test more expressive.

4. What is the difference between `assertEquals`, `assertTrue`, and `assertSame` in JUnit?

`assertEquals` checks value equality, `assertTrue` checks that a condition is true, and `assertSame` checks object identity. In interviews, mention that `assertSame` is about the same object reference, not just equal content.

5. What are parameterized tests in JUnit, and how do they work?

Parameterized tests run the same test logic with multiple input sets. In JUnit 5, use `@ParameterizedTest` with sources such as `@CsvSource`, `@ValueSource`, or `@MethodSource`. They reduce duplication and are ideal for validation and edge-case coverage.

6. What is a test suite in JUnit, and how do you create it?

A test suite groups multiple test classes so they can run together. In JUnit 5, use the JUnit Platform suite API with `@Suite` and `@SelectClasses`. In older JUnit 4 code, you would have used `@RunWith(Suite.class)`.

7. How do you handle timeouts in JUnit?

In JUnit 5, prefer `assertTimeout(...)` or `assertTimeoutPreemptively(...)`. In JUnit 4, `@Test(timeout = ...)` was common. Timeouts protect you from hanging tests and make slow behavior visible quickly.

8. How do you structure a test case in JUnit?

Use Arrange, Act, Assert. Arrange the inputs and dependencies, act by calling the method under test, and assert the expected result. This keeps tests easy to read and easy to debug.

9. What is the purpose of the `@Test` annotation?

`@Test` marks a method as a test method. The test runner discovers it and executes it automatically. Without it, the method is just ordinary code.

10. How do you mock a static method in JUnit? Is it possible without external libraries?

JUnit itself does not mock anything, so static method mocking is not possible with JUnit alone. If you need it, use Mockito with the inline mock maker and `Mockito.mockStatic(...)`. In interviews, also mention that static mocking is usually a design smell and should be avoided when possible.

11. Can you explain how `@RunWith` and `@Rule` work in JUnit?

Both are JUnit 4 concepts. `@RunWith` swaps the default test runner, and `@Rule` adds behavior around test execution, such as temporary folders or expected exceptions. In JUnit 5, the equivalent idea is extensions, usually via `@ExtendWith`.

12. Tricky: How would you test private methods in JUnit? Should you test them directly?

Do not test private methods directly. Test them through the public methods that use them. If a private method feels impossible to cover through the public API, that is often a design signal that the class should be refactored.

13. Tricky: How do you write a test for a method with database calls in JUnit without hitting the actual database?

Mock the repository or DAO dependency and return controlled data from the mock. This keeps tests fast and deterministic. For repository-heavy code, use unit tests for logic and integration tests only when you need a real database.

14. Tricky: How does JUnit handle concurrency when running multiple test methods in parallel?

JUnit 5 can run tests in parallel when configured, but your tests must be thread-safe. Avoid shared mutable state, isolate test data, and do not depend on execution order. Parallel execution is useful for speed, but only when the code and tests are safe for it.

15. What are some best practices for writing unit tests using JUnit?

Keep tests small, independent, and focused on one behavior. Use descriptive names, avoid shared state, prefer deterministic inputs, and let tests document intent. Good tests are as maintainable as production code.

## Mockito Questions

1. What is Mockito, and why is it used in unit testing?

Mockito is a Java mocking framework used to create fake dependencies for unit tests. It helps isolate the class under test from databases, HTTP clients, Kafka producers, and other external systems. That isolation makes tests faster and easier to reason about.

2. How do you mock an object in Mockito?

Use `mock(SomeClass.class)` or the `@Mock` annotation with `MockitoExtension`. Then stub behavior with `when(...).thenReturn(...)` or related APIs. The mock does nothing real until you define its behavior.

3. What is the purpose of the `@Mock` and `@InjectMocks` annotations?

`@Mock` creates a mock dependency, and `@InjectMocks` creates the class under test while injecting those mocks into it. This is convenient for constructor or field injection tests because you avoid manual setup code.

4. How do you use `when` and `thenReturn` in Mockito?

`when` defines the method call you want to stub, and `thenReturn` defines the result. For example, `when(repo.findById("1")).thenReturn(Optional.of(account))`. This lets you control the dependency behavior precisely.

5. What is the difference between `mock()` and `spy()` in Mockito?

`mock()` creates a fake object with no real behavior unless you stub it. `spy()` wraps a real object and uses real behavior by default, while still allowing selective stubbing. Use `spy()` only when partial real behavior is truly useful.

6. How do you mock a method that returns void in Mockito?

Use `doNothing()`, `doThrow()`, or `doAnswer()` with the target method call. Void methods cannot be stubbed with the usual `when(...).thenReturn(...)` style. `doNothing()` is the simplest when you only want to suppress the behavior.

7. What are the use cases for `doReturn()`, `doThrow()`, and `doAnswer()` in Mockito?

Use `doReturn()` when normal stubbing is awkward, `doThrow()` when you want a mocked call to fail, and `doAnswer()` when you need custom logic at call time. These APIs are especially helpful for spies and void methods.

8. How do you verify the behavior of a mock object in Mockito?

Use `verify(mock).method(...)` or more specific forms like `verify(mock, times(2))`. Verification checks that your class interacted with its dependencies the way you expected. This is a core part of interaction-based testing.

9. How do you mock an exception using Mockito?

Use `when(...).thenThrow(...)` for normal methods or `doThrow(...)` for void methods. That allows you to test failure paths without forcing the real dependency to fail.

10. How does `ArgumentCaptor` work in Mockito? Can you give an example?

`ArgumentCaptor` captures the actual argument passed into a mock so you can inspect its fields. It is useful when the value is built inside the class under test and you want to verify the exact object sent to another dependency.

11. Tricky: How do you mock static methods in Mockito?

Use Mockito inline mocking with `try (MockedStatic<SomeClass> mocked = Mockito.mockStatic(SomeClass.class)) { ... }`. That feature isolates static calls for the duration of the try block. If you find yourself needing it often, consider refactoring toward injectable collaborators.

12. Tricky: What is the difference between `verify()` and `verifyNoMoreInteractions()` in Mockito?

`verify()` checks the interactions you explicitly care about. `verifyNoMoreInteractions()` asserts that nothing else happened on the mock. Use the second one carefully because it can make tests brittle if the implementation legitimately changes.

13. Tricky: How do you mock final classes and methods in Mockito? Is it possible in earlier versions of Mockito?

Modern Mockito can mock final classes and methods when the inline mock maker is enabled. Earlier Mockito versions could not do this. If a design depends heavily on final or static behavior, that is often a sign to prefer interfaces or injectable wrappers.

14. Tricky: How would you mock dependencies that are passed to a method as parameters?

Create mock objects for those parameters and pass them into the method under test. The important part is that the class under test should receive controlled test doubles, not real dependencies.

15. Tricky: How do you handle method chaining such as `foo.bar().baz()` in Mockito?

The safer approach is to avoid deep chains in production code and return a simpler object instead. If you cannot change the design, you can stub each level or use deep stubs. Deep stubs work, but they are usually a last resort.

16. What is the difference between a stub and a mock?

A stub returns controlled data and is mainly about state. A mock both returns controlled data and verifies interactions. In interviews, the short version is: stubs answer questions, mocks check behavior.

17. How do you mock objects in Mockito when using constructor injection?

Create the mocks first, then pass them directly into the constructor of the class under test. Constructor injection is the cleanest way to use Mockito because it makes dependencies explicit and easy to replace.

18. Tricky: Can you explain Mockito's `RETURNS_DEEP_STUBS` and its use case?

`RETURNS_DEEP_STUBS` automatically creates mocks for chained calls. It can reduce setup code for nested object graphs, but it also hides design complexity. Use it only when you cannot simplify the production API.

19. Tricky: How do you mock behavior for methods that depend on randomness such as `Math.random()`?

Do not mock randomness directly in most cases. Wrap the random value in a dedicated class or interface, inject that collaborator, and mock the wrapper. That makes the test deterministic and the production code easier to maintain.

20. How do you combine JUnit and Mockito to write comprehensive unit tests?

Use JUnit for test structure and assertions, and Mockito for dependency isolation and interaction verification. This combination gives you fast tests that focus on one class at a time.

## Interview reminders

- Prefer JUnit 5 terminology in new projects.
- Mention that Mockito is for dependency isolation, not for replacing integration tests.
- If asked about private methods or static calls, point out the design trade-off first, then the testing workaround.
- For Spring Boot 3, the practical stack is JUnit 5 + Mockito + AssertJ.