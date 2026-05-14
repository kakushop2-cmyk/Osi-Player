package com.nativeplayer.ui.home;

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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<WatchHistoryRepository> watchHistoryRepositoryProvider;

  public HomeViewModel_Factory(Provider<WatchHistoryRepository> watchHistoryRepositoryProvider) {
    this.watchHistoryRepositoryProvider = watchHistoryRepositoryProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(watchHistoryRepositoryProvider.get());
  }

  public static HomeViewModel_Factory create(
      Provider<WatchHistoryRepository> watchHistoryRepositoryProvider) {
    return new HomeViewModel_Factory(watchHistoryRepositoryProvider);
  }

  public static HomeViewModel newInstance(WatchHistoryRepository watchHistoryRepository) {
    return new HomeViewModel(watchHistoryRepository);
  }
}
