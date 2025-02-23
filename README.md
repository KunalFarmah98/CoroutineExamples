# CoroutineExamples: A Practical Exploration of Coroutine Concepts

This project is designed to be a practical guide to understanding how coroutines work, with a focus on scopes, builders, dispatchers, and error handling.

## Core Concepts Illustrated

The `CoroutineUtils.kt` file primarily explores the following key coroutine concepts:

1.  **Coroutine Scopes:**
    *   **Purpose:** Coroutine scopes define the lifetime and context in which coroutines are launched. They control the coroutine's lifecycle and allow for structured concurrency.
    *   **Demonstrated in `CoroutineUtils.kt`:**
        *   `CoroutineScope()`: Creating custom coroutine scopes for specific purposes.
        *   `customSupervisorScope`: Illustrates how to create a custom supervisorScope, that can allow errors to be handled by each child coroutines and don't stop other children.
        *   How scopes control the parent-child relationship and error propagation between coroutines.
    *   **Key Takeaways:**
        *   Using appropriate scopes ensures that coroutines are canceled when their work is no longer needed, preventing resource leaks.
        *   Scopes enable structured concurrency, which makes coroutine management more predictable.

2.  **Coroutine Builders:**
    *   **Purpose:** Coroutine builders are functions that launch new coroutines. They differ in how they structure the coroutine and handle return values.
    *   **Demonstrated in `CoroutineUtils.kt`:**
        *   `launch`: Used to start a coroutine that does not return a value (fire-and-forget).
        *   `async`: Used to start a coroutine that returns a value (like a future or promise).
    *   **Key Takeaways:**
        *   `launch` is for starting coroutines that run independently.
        *   `async` is for starting coroutines that produce results that you need to retrieve later.

3.  **Dispatchers:**
    *   **Purpose:** Dispatchers determine which thread or thread pool a coroutine will run on.
    *   **Demonstrated in `CoroutineUtils.kt`:**
        *   `Dispatchers.Default`: For CPU-intensive tasks.
        *   How to use different dispatchers within the same coroutine to optimize for different types of work.
    *   **Key Takeaways:**
        *   Use `Dispatchers.Default` for CPU-bound tasks to utilize multi-core processing.
        *   Understanding dispatchers helps avoid blocking the main thread and improve responsiveness.

4.  **Exception Handling:**
    *   **Purpose:** Managing exceptions that occur within coroutines to prevent crashes and ensure proper resource cleanup.
    *   **Demonstrated in `CoroutineUtils.kt`:**
        *   `CoroutineExceptionHandler`: Handling uncaught exceptions at the scope level.
        *   `try-catch`: Handling exceptions within a coroutine block.
        * `supervisorScope`: demonstrating how the supervisor scope can isolate exceptions and prevent them from crashing their parent coroutines.
        * Handling exceptions when using both `launch` and `async`
        * `customSupervisorScope`: Creating a custom supervisor scope.
    *   **Key Takeaways:**
        *   Proper exception handling is crucial for the stability of concurrent applications.
        *   `CoroutineExceptionHandler` allows for centralized exception handling for entire scopes.
        *   `supervisorScope` isolate exceptions to a single child coroutine, preventing them from breaking other coroutines.

5. **Structured concurrency**
    *   **Purpose:** Structured concurrency is a paradigm that allows coroutines to be managed in a structured way. This means that you can easily see which coroutines are running at any given time, and that they will all be cancelled when the scope they are running in is cancelled.
    * **Demonstrated in `CoroutineUtils.kt`:**
        * The way scopes are used create a parent-child relationship, allowing structured concurrency.
        * Launching a coroutine inside another coroutine automatically makes them part of the same structure, with the child being managed by the parent.
        * How supervisorScope or customSupervisorScope can be used to further refine this structure.
    * **Key Takeaways:**
        *  Structured concurrency make managing large projects with a lot of coroutines much easier.
        * It is a way to ensure that coroutines will not leak.
        * It allows to control the flow of coroutines and easily visualize the relationships between them.
6. **Parent and child coroutines**
    * **Purpose**: When a coroutine launches another coroutine, the first one becomes the parent and the second one the child.
    * **Demonstrated in `CoroutineUtils.kt`**:
        * when a launch block launch another child block, you can observe how their respective jobs and parents are created.
    * **Key takeaways**:
        * the job of a parent will not be completed until the jobs of all its children are completed.
        * An exception thrown by the child will be propagated to the parent if the parent is not a supervisorScope.

7. **Job cancellation**
    * **Purpose**: Cancelling jobs that are not needed anymore can be used to improve performance, and ensure that no ressources are wasted.
    * **Demonstrated in `CoroutineUtils.kt`**:
        * how launching an async or launch block returns a Job.
        * how the Job has an `invokeOnCompletion` method that allow listening for the completion or cancellation of the job.
    * **Key takeaways**:
        * Jobs can be cancelled manually, or when their scopes is cancelled.
        * Listening for cancellation is a good way to track errors.

## Case Studies within `CoroutineUtils.kt`

The file provides several case studies (functions) that illustrate the above concepts in different combinations:

*   `exceptionInChildSupervisorScopeLaunchedInsideAsyncCoroutineScope`: demonstrate how supervisor scopes can be used to prevent crashes, and handle errors independently.
*   `exceptionInChildSupervisorScopeLaunchedInsideAsyncCoroutineScope2`: Demonstrate how `customSupervisorScope` can be used to create custom supervisorScope.
*   `exceptionInChildSupervisorScopeLaunchedInsideAsyncCoroutineScope3`: Demonstrate how to use `supervisorScope` with `launch` blocks and structured concurrency.
*   `exceptionInChildSupervisorScopeLaunchedInsideAsyncCoroutineScope4`: show how using the suspending `supervisorScope` function in a synchronous way affect the flow of the code.

**How to Explore the Code:**

1.  **Read the Function Names:** The function names are designed to be descriptive of the scenario being demonstrated.
2.  **Examine the Code Blocks:** Each function contains `launch` or `async` blocks, often with nested blocks, to show how coroutines interact.
3.  **Look for Dispatcher Usage:** See how different dispatchers are used for different types of work.
4.  **Identify Exception Handling:** Pay attention to `try-catch` blocks and the use of `CoroutineExceptionHandler`.
5. **Check for Jobs:** check how each coroutine has a specific job, and how they are related to their parent's job.
6. **Follow the Log statements**: each function logs actions to the console, making it easier to understand the order of events.

## Further Resources

*   [Kotlin Coroutines Documentation](https://kotlinlang.org/docs/coroutines-overview.html)
*   [Android Coroutines Guide](https://developer.android.com/kotlin/coroutines)
* [Android Coroutines codelab](https://developer.android.com/codelabs/kotlin-coroutines)
