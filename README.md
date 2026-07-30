# Random Users Android Test

A Jetpack Compose application that loads deterministic user pages from `randomuser.me`, prefetches when three rows remain, and stops after three pages.

## Requirements

- Android Studio with Android SDK 36 installed
- JDK 21 (the Gradle daemon toolchain can provision a compatible JDK)
- An emulator or device running Android 9 (API 28) or newer

Open the project in Android Studio, allow Gradle synchronization to finish, and run the `app` configuration.

## Architecture

The project uses conventional Android Clean Architecture package boundaries:

- `data/remote/api` - Retrofit endpoint definitions
- `data/remote/dto` - network response models
- `data/remote` - remote data source
- `data/mapper` - DTO-to-domain conversion
- `data/repository` - repository implementation
- `domain/model` - framework-independent business models
- `domain/repository` - repository contracts
- `domain/usecase` - feature business rules and paging policy
- `presentation/users` - ViewModel, UI state, and screen
- `presentation/users/components` - reusable user-list composables
- `presentation/navigation` - typed routes, feature graphs, and root `NavHost`
- `presentation/theme` - Compose theme
- `di` - Koin dependency declarations

Dependencies point inward: presentation depends on domain contracts and use cases, while data implements domain repositories. Koin connects implementations at the application boundary. The users destination obtains its ViewModel with `koinViewModel()`.

## Pagination

The first 20 users load automatically. When the 17th row becomes visible, the next page is requested so that three rows remain available while loading. Pagination stops after three successful pages, producing 60 unique users.

The application uses zero-based page indexes `0`, `1`, and `2`. Because Random User uses one-based pagination and treats `page=0` as an alias for its first page, the remote data source translates those indexes to API pages `1`, `2`, and `3`. User IDs are deduplicated when pages are merged.

## Verification

Run lint, JVM unit tests, and assemble both the application and instrumentation-test APKs:

```powershell
.\gradlew.bat lintDebug testDebugUnitTest assembleDebug assembleDebugAndroidTest
```

Unit tests cover API mapping and parameters, initial loading, the exact prefetch threshold, the three-page cap, retry behavior, and use-case validation. Compose instrumentation tests cover loading, populated, error, and root navigation states.

Running instrumentation tests requires an emulator or connected device:

```powershell
.\gradlew.bat connectedDebugAndroidTest
```

## AI prompt evidence

Screenshots of the prompts used throughout development are available in [`prompts_screenshots`](prompts_screenshots).
