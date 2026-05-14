package com.nativeplayer.ui.player;

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

  public PlayerViewModel_Factory(Provider<WatchHistoryRepository> watchHistoryRepositoryProvider) {
    this.watchHistoryRepositoryProvider = watchHistoryRepositoryProvider;
  }

  @Override
  public PlayerViewModel get() {
    return newInstance(watchHistoryRepositoryProvider.get());
  }

  public static PlayerViewModel_Factory create(
      Provider<WatchHistoryRepository> watchHistoryRepositoryProvider) {
    return new PlayerViewModel_Factory(watchHistoryRepositoryProvider);
  }

  public static PlayerViewModel newInstance(WatchHistoryRepository watchHistoryRepository) {
    return new PlayerViewModel(watchHistoryRepository);
  }
}
