package com.nativeplayer.ui.player;

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
public final class PlayerViewModel_Factory implements Factory<PlayerViewModel> {
  private final Provider<WatchHistoryRepository> watchHistoryRepositoryProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  public PlayerViewModel_Factory(Provider<WatchHistoryRepository> watchHistoryRepositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    this.watchHistoryRepositoryProvider = watchHistoryRepositoryProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public PlayerViewModel get() {
    return newInstance(watchHistoryRepositoryProvider.get(), preferencesManagerProvider.get());
  }

  public static PlayerViewModel_Factory create(
      Provider<WatchHistoryRepository> watchHistoryRepositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    return new PlayerViewModel_Factory(watchHistoryRepositoryProvider, preferencesManagerProvider);
  }

  public static PlayerViewModel newInstance(WatchHistoryRepository watchHistoryRepository,
      PreferencesManager preferencesManager) {
    return new PlayerViewModel(watchHistoryRepository, preferencesManager);
  }
}
