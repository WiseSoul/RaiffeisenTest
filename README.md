# Random Users Android Test

A Jetpack Compose application that loads deterministic user pages from `randomuser.me`, prefetches when three rows remain, and stops after three pages.

## Architecture

The project uses conventional Android Clean Architecture package boundaries:

- `data/remote/api` - Retrofit endpoint definitions
- `data/remote/dto` - network response models
- `data/remote` - remote data source
- `data/repository` - repository implementation and DTO-to-domain mapping
- `domain/model` - framework-independent business models
- `domain/repository` - repository contracts
- `domain/usecase` - feature business rules and paging policy
- `presentation/users` - ViewModel, UI state, and screen
- `presentation/users/components` - reusable user-list composables
- `presentation/navigation` - typed routes, feature graphs, and root `NavHost`
- `presentation/theme` - Compose theme
- `di` - Koin dependency declarations

Dependencies point inward: presentation depends on domain contracts and use cases, while data implements domain repositories. Koin connects implementations at the application boundary. The users destination obtains its ViewModel with `koinViewModel()`.

## Verification

```powershell
.\gradlew.bat lintDebug testDebugUnitTest assembleDebug assembleDebugAndroidTest
```

Unit tests cover API mapping, initial loading, the exact prefetch threshold, the three-page cap, retry behavior, and use-case validation. Compose instrumentation tests cover loading, populated, error, and root navigation states. Running instrumentation tests requires an emulator or connected device:

```powershell
.\gradlew.bat connectedDebugAndroidTest
```
