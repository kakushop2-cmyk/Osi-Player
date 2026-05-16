package com.nativeplayer.ui.settings;

import com.nativeplayer.data.model.PlayerSettings;
import com.nativeplayer.data.repository.PreferencesManager;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.ExperimentalCoroutinesApi;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

@org.junit.runner.RunWith(value = org.robolectric.RobolectricTestRunner.class)
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\f\u0010\t\u001a\u00060\nj\u0002`\u000bH\u0007J\f\u0010\f\u001a\u00060\nj\u0002`\u000bH\u0007J\f\u0010\r\u001a\u00060\nj\u0002`\u000bH\u0007J\f\u0010\u000e\u001a\u00060\nj\u0002`\u000bH\u0007J\b\u0010\u000f\u001a\u00020\nH\u0007J\b\u0010\u0010\u001a\u00020\nH\u0007J\b\u0010\u0011\u001a\u00020\nH\u0007R\u0012\u0010\u0003\u001a\u00020\u00048\u0002@\u0002X\u0083.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/nativeplayer/ui/settings/PlayerSettingsViewModelTest;", "", "()V", "preferencesManager", "Lcom/nativeplayer/data/repository/PreferencesManager;", "testDispatcher", "Lkotlinx/coroutines/test/TestDispatcher;", "viewModel", "Lcom/nativeplayer/ui/settings/PlayerSettingsViewModel;", "save() should emit false when preferences save fails", "", "Lkotlinx/coroutines/test/TestResult;", "save() should emit true on successful save", "save() should emit true when preferences save succeeds", "save() should validate and coerce settings values", "setup", "tearDown", "update() should reflect new settings immediately", "app_debugUnitTest"})
@kotlin.OptIn(markerClass = {kotlinx.coroutines.ExperimentalCoroutinesApi.class})
public final class PlayerSettingsViewModelTest {
    @org.mockito.Mock()
    private com.nativeplayer.data.repository.PreferencesManager preferencesManager;
    private com.nativeplayer.ui.settings.PlayerSettingsViewModel viewModel;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.test.TestDispatcher testDispatcher = null;
    
    public PlayerSettingsViewModelTest() {
        super();
    }
    
    @org.junit.Before()
    public final void setup() {
    }
    
    @org.junit.After()
    public final void tearDown() {
    }
}