package com.nativeplayer.ui.splash;

import com.nativeplayer.data.repository.IptvRepository;
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
public final class SplashViewModel_Factory implements Factory<SplashViewModel> {
  private final Provider<IptvRepository> repositoryProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  public SplashViewModel_Factory(Provider<IptvRepository> repositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    this.repositoryProvider = repositoryProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public SplashViewModel get() {
    return newInstance(repositoryProvider.get(), preferencesManagerProvider.get());
  }

  public static SplashViewModel_Factory create(Provider<IptvRepository> repositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    return new SplashViewModel_Factory(repositoryProvider, preferencesManagerProvider);
  }

  public static SplashViewModel newInstance(IptvRepository repository,
      PreferencesManager preferencesManager) {
    return new SplashViewModel(repository, preferencesManager);
  }
}
