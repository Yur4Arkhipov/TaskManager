# Repository Guidelines

## Project Structure & Module Organization

```
taskmanager/
├── app/                          # Entry point: MainActivity, AppNavHost, theme setup
├── core/                         # Shared modules
│   ├── data/                     # Repository interfaces & implementations
│   ├── database/                 # Room: database, entities, DAO's
│   ├── datastore/                # DataStore for app preferences
│   ├── designsystem/             # Reusable UI components, theme, icons
│   ├── domain/                   # Use cases (business logic)
│   ├── model/                    # Shared data models & events
│   ├── network/                  # Retrofit, OkHttp, API's
│   ├── speech/                   # Android SpeechRecognizer wrapper
│   └── storage/                  # audio recording, image storage, file handling
├── feature/                      # Feature modules
│   ├── notes/                    # Notes list + editor
│   ├── tasks/                    # Tasks list
│   └── settings/                 # Theme & palette settings, audio input mode
├── config/detekt/                # Detekt static analysis config
└── docs/                         # Screenshots and architecture diagrams
```

**Dependency direction:** `feature` → `core:domain` → `core:data` → `core:database` / `core:datastore` / `core:speech` / `core:storage` / `core:network`. Feature modules depend on `core:domain` (use cases) and `core:model` (types), never on `core:data` directly.

## Build, Test, and Development Commands

| Command | Description |
|---------|-------------|
| `.\gradlew assembleDebug` | Build debug APK |
| `.\gradlew assembleRelease` | Build release APK (unsigned) |
| `.\gradlew connectedAndroidTest` | Run instrumentation tests on connected device |
| `.\gradlew test` | Run local unit tests |
| `.\gradlew detekt` | Run static analysis |
| `.\gradlew detektFormat` | Auto-format code via Detekt |

Run from the project root. Use Android Studio's Run/Debug configurations for running the app.

## Coding Style & Naming Conventions

- **Language:** Kotlin (target 2.4+)
- **Style:** Follows [kotlinlang.org coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- **Formatting:** Android Studio default Kotlin formatter
- **Static analysis:** [Detekt](https://detekt.dev/) configured in `config/detekt/detekt.yml` with a baseline file. Run `gradlew detekt` before committing.
- **Naming patterns:**
  - Classes: `PascalCase` — `NoteEditorViewModel`, `AudioRecorderImpl`
  - Interfaces: `PascalCase` without prefix — `AudioRecorder`, `SystemSpeechRecognizer`
  - Implementations: `<Interface>Impl` — `AudioRecorderImpl`
  - Use cases: `<Verb>UseCase` — `StartVoiceRecordingUseCase`
  - Repositories: `<Domain>Repository` — `SpeechRecognitionRepository`
  - Compose UI state: `*State` sealed interfaces — `VoiceState`, `NoteEditorUiState`
  - Events/actions: `*Event` / `*Clicked` sealed interfaces — `NoteEditorEvent`

## Testing Guidelines

- **Frameworks:** JUnit 4, MockK, Turbine (for Flow testing), Truth
- **Test location:** `src/test/kotlin/` alongside source files
- **Test naming:** `<ClassBeingTested>Test` — `NoteEditorViewModelTest`
- **Run tests:** `.\gradlew test` (local) or `.\gradlew connectedAndroidTest` (instrumentation)
- Coverage is not enforced yet. Prioritize unit tests for use cases and ViewModel logic.

## Commit & Pull Request Guidelines

- **Commit messages:** Conventional-style, imperative mood, short prefix:
  - `fix: speech recognizer for tasks screen`
  - `feat: add voice input mode to user settings`
  - `refactor: optimize usecase, make it universal for ui`
- Prefixes used: `feat`, `fix`, `refactor`, `chore`, `docs`
- Pull requests reference task IDs (e.g., `tm-12`, `tm-11`) in branch names and merge commits.

## Architecture Overview

The app follows Google's recommended architecture:

1. **UI Layer** (Compose) → observes `StateFlow` from ViewModel
2. **ViewModel** → exposes UI state, handles user actions, calls use cases
3. **Domain Layer** (use cases) → single-responsibility business logic, no Android dependencies
4. **Data Layer** (repositories) → collect all data sources (from database, datastore, network and others)
5. **Network/Storage/Speech/Database/Datastore** → concrete implementations of platform integrations

Dependency injection is handled by Hilt. Each module declares its own `@Module` with `@InstallIn` bindings.

## Security & Configuration

- Audio recording requires `RECORD_AUDIO` runtime permission.
- Min SDK: 26 (Android 8.0).
