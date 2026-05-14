package com.nativeplayer.data.repository;

import android.content.SharedPreferences;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class PreferencesManager_Factory implements Factory<PreferencesManager> {
  private final Provider<SharedPreferences> prefsProvider;

  public PreferencesManager_Factory(Provider<SharedPreferences> prefsProvider) {
    this.prefsProvider = prefsProvider;
  }

  @Override
  public PreferencesManager get() {
    return newInstance(prefsProvider.get());
  }

  public static PreferencesManager_Factory create(Provider<SharedPreferences> prefsProvider) {
    return new PreferencesManager_Factory(prefsProvider);
  }

  public static PreferencesManager newInstance(SharedPreferences prefs) {
    return new PreferencesManager(prefs);
  }
}
