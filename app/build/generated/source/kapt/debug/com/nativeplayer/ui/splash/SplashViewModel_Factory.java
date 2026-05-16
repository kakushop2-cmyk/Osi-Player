package com.nativeplayer.ui.splash;

import com.nativeplayer.data.repository.IptvRepository;
import com.nativeplayer.data.repository.PreferencesManager;
import com.nativeplayer.data.repository.WatchHistoryRepository;
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

  private final Provider<WatchHistoryRepository> watchHistoryRepositoryProvider;

  public SplashViewModel_Factory(Provider<IptvRepository> repositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider,
      Provider<WatchHistoryRepository> watchHistoryRepositoryProvider) {
    this.repositoryProvider = repositoryProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
    this.watchHistoryRepositoryProvider = watchHistoryRepositoryProvider;
  }

  @Override
  public SplashViewModel get() {
    return newInstance(repositoryProvider.get(), preferencesManagerProvider.get(), watchHistoryRepositoryProvider.get());
  }

  public static SplashViewModel_Factory create(Provider<IptvRepository> repositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider,
      Provider<WatchHistoryRepository> watchHistoryRepositoryProvider) {
    return new SplashViewModel_Factory(repositoryProvider, preferencesManagerProvider, watchHistoryRepositoryProvider);
  }

  public static SplashViewModel newInstance(IptvRepository repository,
      PreferencesManager preferencesManager, WatchHistoryRepository watchHistoryRepository) {
    return new SplashViewModel(repository, preferencesManager, watchHistoryRepository);
  }
}
