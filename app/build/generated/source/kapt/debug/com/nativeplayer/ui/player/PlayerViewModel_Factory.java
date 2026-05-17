package com.nativeplayer.ui.player;

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
public final class PlayerViewModel_Factory implements Factory<PlayerViewModel> {
  private final Provider<WatchHistoryRepository> watchHistoryRepositoryProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  private final Provider<IptvRepository> iptvRepositoryProvider;

  public PlayerViewModel_Factory(Provider<WatchHistoryRepository> watchHistoryRepositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider,
      Provider<IptvRepository> iptvRepositoryProvider) {
    this.watchHistoryRepositoryProvider = watchHistoryRepositoryProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
    this.iptvRepositoryProvider = iptvRepositoryProvider;
  }

  @Override
  public PlayerViewModel get() {
    return newInstance(watchHistoryRepositoryProvider.get(), preferencesManagerProvider.get(), iptvRepositoryProvider.get());
  }

  public static PlayerViewModel_Factory create(
      Provider<WatchHistoryRepository> watchHistoryRepositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider,
      Provider<IptvRepository> iptvRepositoryProvider) {
    return new PlayerViewModel_Factory(watchHistoryRepositoryProvider, preferencesManagerProvider, iptvRepositoryProvider);
  }

  public static PlayerViewModel newInstance(WatchHistoryRepository watchHistoryRepository,
      PreferencesManager preferencesManager, IptvRepository iptvRepository) {
    return new PlayerViewModel(watchHistoryRepository, preferencesManager, iptvRepository);
  }
}
