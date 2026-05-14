package com.nativeplayer.di;

import android.content.SharedPreferences;
import com.nativeplayer.data.repository.PreferencesManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvidePreferencesManagerFactory implements Factory<PreferencesManager> {
  private final Provider<SharedPreferences> prefsProvider;

  public AppModule_ProvidePreferencesManagerFactory(Provider<SharedPreferences> prefsProvider) {
    this.prefsProvider = prefsProvider;
  }

  @Override
  public PreferencesManager get() {
    return providePreferencesManager(prefsProvider.get());
  }

  public static AppModule_ProvidePreferencesManagerFactory create(
      Provider<SharedPreferences> prefsProvider) {
    return new AppModule_ProvidePreferencesManagerFactory(prefsProvider);
  }

  public static PreferencesManager providePreferencesManager(SharedPreferences prefs) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.providePreferencesManager(prefs));
  }
}
