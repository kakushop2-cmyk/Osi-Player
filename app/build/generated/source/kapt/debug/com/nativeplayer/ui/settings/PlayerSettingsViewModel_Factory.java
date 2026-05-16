package com.nativeplayer.ui.settings;

import com.nativeplayer.data.repository.PreferencesManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class PlayerSettingsViewModel_Factory implements Factory<PlayerSettingsViewModel> {
  private final Provider<PreferencesManager> preferencesManagerProvider;

  public PlayerSettingsViewModel_Factory(Provider<PreferencesManager> preferencesManagerProvider) {
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public PlayerSettingsViewModel get() {
    return newInstance(preferencesManagerProvider.get());
  }

  public static PlayerSettingsViewModel_Factory create(
      Provider<PreferencesManager> preferencesManagerProvider) {
    return new PlayerSettingsViewModel_Factory(preferencesManagerProvider);
  }

  public static PlayerSettingsViewModel newInstance(PreferencesManager preferencesManager) {
    return new PlayerSettingsViewModel(preferencesManager);
  }
}
