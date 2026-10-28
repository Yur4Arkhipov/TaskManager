# AGENTS — feature:settings

## Purpose
The `feature:settings` module provides the app settings screen: color palette selection, theme mode, and voice input mode.

## Quick Navigation
- Entry point -> `presentation/SettingsScreen.kt`
- State & logic -> `presentation/SettingsViewModel.kt`
- Navigation -> `navigation/SettingsNavigation.kt`

## Architecture
```
:core:data
    ↓
ObserveUserDataUseCase
    ↓
SettingsViewModel
    ↓
SettingsScreen
```
