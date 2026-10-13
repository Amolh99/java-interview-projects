# Module 19 - JUnit 5 & Mockito

Testing an order checkout flow with collaborators replaced by mocks. The tests cover the happy path, failure paths, argument capture, and parameterized cases.

## Concepts
- **Unit vs integration:** unit tests mock collaborators. Integration tests use real ones.
- **Mocks, stubs, spies:** `when(...).thenReturn` stubs. `verify` checks interactions.
- **Argument capture:** `ArgumentCaptor` inspects what was passed to a mock.
- **Parameterized tests:** `@ParameterizedTest` runs one test over many inputs.
- **Strict stubs:** `MockitoExtension` fails tests that stub calls they never make.

## Interview questions
1. **Mock vs spy?** A mock has no real behaviour unless stubbed. A spy wraps a real object.
2. **Why `verifyNoMoreInteractions`?** It catches calls you did not intend.
3. **Can you mock a `final` class?** Mockito 5 uses inline mock making, so yes. Still prefer designing for interfaces.
4. **`@Mock` vs `@InjectMocks`?** `@Mock` creates a fake. `@InjectMocks` builds the class under test and injects the mocks.
5. **Why avoid mocking value objects?** Use the real thing. Mocks hide logic.

## Run
```bash
mvn test
```
