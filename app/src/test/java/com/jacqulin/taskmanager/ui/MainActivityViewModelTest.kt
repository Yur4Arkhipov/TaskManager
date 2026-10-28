package com.jacqulin.taskmanager.ui

import com.jacqulin.taskmanager.MainActivityViewModel
import com.jacqulin.taskmanager.core.model.ColorPalette
import com.jacqulin.taskmanager.core.model.DarkThemeConfig
import com.jacqulin.taskmanager.data.domain.AppSettingsRepository
import com.jacqulin.taskmanager.data.model.AppSettings
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainActivityViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val appSettingsRepository = mockk<AppSettingsRepository>()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial ui state should be Loading`() {
        val viewModel = MainActivityViewModel(appSettingsRepository)
        assertTrue(viewModel.uiState.value is MainActivityUiState.Loading)
    }

    @Test
    fun `ui state should transition to Success when settings are loaded`() = runTest {
        val settings = AppSettings(
            darkThemeConfig = DarkThemeConfig.DARK,
            colorPalette = ColorPalette.DEFAULT
        )
        val settingsFlow: Flow<AppSettings> = flow { emit(settings) }
        coEvery { appSettingsRepository.appSettings } returns settingsFlow

        val viewModel = MainActivityViewModel(appSettingsRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        val uiState = viewModel.uiState.value
        assertTrue(uiState is MainActivityUiState.Success)
        assertEquals(DarkThemeConfig.DARK, (uiState as MainActivityUiState.Success).settings.darkThemeConfig)
    }

    @Test
    fun `shouldKeepSplashScreen should return true when Loading`() {
        val viewModel = MainActivityViewModel(appSettingsRepository)
        assertTrue(viewModel.uiState.value.shouldKeepSplashScreen())
    }

    @Test
    fun `shouldKeepSplashScreen should return false when Success`() = runTest {
        val settings = AppSettings()
        val settingsFlow: Flow<AppSettings> = flow { emit(settings) }
        coEvery { appSettingsRepository.appSettings } returns settingsFlow

        val viewModel = MainActivityViewModel(appSettingsRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.shouldKeepSplashScreen())
    }

    @Test
    fun `shouldUseDarkTheme should return false when Loading`() {
        val viewModel = MainActivityViewModel(appSettingsRepository)
        assertFalse(viewModel.uiState.value.shouldUseDarkTheme(isSystemDarkTheme = true))
    }

    @Test
    fun `getColorPalette should return DEFAULT when Loading`() {
        val viewModel = MainActivityViewModel(appSettingsRepository)
        assertEquals(ColorPalette.DEFAULT, viewModel.uiState.value.getColorPalette())
    }

    @Test
    fun `shouldUseDarkTheme FOLLOW_SYSTEM follows system dark theme`() = runTest {
        val settings = AppSettings(
            darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
            colorPalette = ColorPalette.DEFAULT
        )
        val settingsFlow: Flow<AppSettings> = flow { emit(settings) }
        coEvery { appSettingsRepository.appSettings } returns settingsFlow

        val viewModel = MainActivityViewModel(appSettingsRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        val uiState = viewModel.uiState.value as MainActivityUiState.Success
        assertTrue(uiState.shouldUseDarkTheme(isSystemDarkTheme = true))
        assertFalse(uiState.shouldUseDarkTheme(isSystemDarkTheme = false))
    }

    @Test
    fun `shouldUseDarkTheme LIGHT always returns false`() = runTest {
        val settings = AppSettings(
            darkThemeConfig = DarkThemeConfig.LIGHT,
            colorPalette = ColorPalette.DEFAULT
        )
        val settingsFlow: Flow<AppSettings> = flow { emit(settings) }
        coEvery { appSettingsRepository.appSettings } returns settingsFlow

        val viewModel = MainActivityViewModel(appSettingsRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        val uiState = viewModel.uiState.value as MainActivityUiState.Success
        assertFalse(uiState.shouldUseDarkTheme(isSystemDarkTheme = true))
        assertFalse(uiState.shouldUseDarkTheme(isSystemDarkTheme = false))
    }

    @Test
    fun `shouldUseDarkTheme DARK always returns true`() = runTest {
        val settings = AppSettings(
            darkThemeConfig = DarkThemeConfig.DARK,
            colorPalette = ColorPalette.DEFAULT
        )
        val settingsFlow: Flow<AppSettings> = flow { emit(settings) }
        coEvery { appSettingsRepository.appSettings } returns settingsFlow

        val viewModel = MainActivityViewModel(appSettingsRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        val uiState = viewModel.uiState.value as MainActivityUiState.Success
        assertTrue(uiState.shouldUseDarkTheme(isSystemDarkTheme = true))
        assertTrue(uiState.shouldUseDarkTheme(isSystemDarkTheme = false))
    }
}
