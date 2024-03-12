## Spring Generic AOP retry on exception

### Description
This Spring Boot auto-configure library provides a reusable aspect `RetryUntilAspect` that enables retry logic for methods annotated with `@RetryUntil`. The aspect is designed to work with a `RetryUntilHelper` component, which handles the retry mechanism based on the specified configuration.

### Features
- **RetryUntilAspect**: An aspect that intercepts methods annotated with `@RetryUntil` and applies retry logic based on the annotation parameters.
- **RetryUntil Annotation**: An annotation `@RetryUntil` that allows specifying the type of retry behavior (non-blocking, blocking, or forever) and timeout settings.
- **RetryUntilHelper Component**: A helper component that encapsulates the retry logic implementation.
- **Retry Strategies**:
    - Non-blocking: Retries without blocking the thread.
    - Blocking: Retries while blocking the thread.
    - Timeout Never: Retries indefinitely until success.

### Usage
1. **Add Dependency**: Include this library as a dependency in your Spring Boot project.
2. **Annotate Methods**: Annotate the methods that require retry logic with `@RetryUntil`.
3. **Configure Retry Behavior**: Specify the retry type and timeout settings in the `@RetryUntil` annotation.
4. **Access RetryUntilHelper**: The `RetryUntilHelper` bean is available for use in your application to handle retry operations.

### Contribution
Feel free to contribute by forking the repository, making changes, and creating a pull request. Your feedback and contributions are welcome!

### License
This library is released under the [MIT License](https://opensource.org/licenses/MIT).