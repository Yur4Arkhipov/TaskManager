# Task Manager Project

TaskManager is a native Android mobile application for managing tasks and notes with manually or
voice recording - users can choose this option.

## Architecture
 
This project is modern Android application that follows the official architecture guidance from Google.
It is a reactive, single-activity app that uses following:

-   **UI:** Built entirely with Jetpack Compose, including Material 3 components and adaptive layouts for different screen sizes.
-   **State Management:** Unidirectional Data Flow (UDF) is implemented using Kotlin Coroutines and `Flow`s. `ViewModel`s act as state holders, exposing UI state as streams of data.
-   **Dependency Injection:** Hilt is used for dependency injection throughout the app, simplifying the management of dependencies and improving testability.
-   **Navigation:** Navigation is handled by Jetpack Navigation 2 for Compose, allowing for a declarative and type-safe way to navigate between screens.
-   **Data:** The data layer is implemented using the repository pattern.
    -   **Local Data:** Room and DataStore are used for local data persistence.
    -   **Remote Data:** Retrofit and OkHttp are used for fetching data from the network.
-   **Background Processing:** WorkManager is used for deferrable background tasks.

## Modules

The main Android app lives in the `app/` folder.
Feature modules live in `feature/` and core and shared modules in `core/`.

## Before modifying code

1. Inspect the existing implementation.
2. Follow existing patterns.
3. Do not introduce a new library if an existing dependency solves the problem.
4. Keep changes focused.
