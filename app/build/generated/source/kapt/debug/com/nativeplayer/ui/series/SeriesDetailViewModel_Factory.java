package com.nativeplayer.ui.series;

import com.nativeplayer.data.repository.FavoriteRepository;
import com.nativeplayer.data.repository.IptvRepository;
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
public final class SeriesDetailViewModel_Factory implements Factory<SeriesDetailViewModel> {
  private final Provider<IptvRepository> iptvRepositoryProvider;

  private final Provider<FavoriteRepository> favoriteRepositoryProvider;

  private final Provider<WatchHistoryRepository> watchHistoryRepositoryProvider;

  public SeriesDetailViewModel_Factory(Provider<IptvRepository> iptvRepositoryProvider,
      Provider<FavoriteRepository> favoriteRepositoryProvider,
      Provider<WatchHistoryRepository> watchHistoryRepositoryProvider) {
    this.iptvRepositoryProvider = iptvRepositoryProvider;
    this.favoriteRepositoryProvider = favoriteRepositoryProvider;
    this.watchHistoryRepositoryProvider = watchHistoryRepositoryProvider;
  }

  @Override
  public SeriesDetailViewModel get() {
    return newInstance(iptvRepositoryProvider.get(), favoriteRepositoryProvider.get(), watchHistoryRepositoryProvider.get());
  }

  public static SeriesDetailViewModel_Factory create(
      Provider<IptvRepository> iptvRepositoryProvider,
      Provider<FavoriteRepository> favoriteRepositoryProvider,
      Provider<WatchHistoryRepository> watchHistoryRepositoryProvider) {
    return new SeriesDetailViewModel_Factory(iptvRepositoryProvider, favoriteRepositoryProvider, watchHistoryRepositoryProvider);
  }

  public static SeriesDetailViewModel newInstance(IptvRepository iptvRepository,
      FavoriteRepository favoriteRepository, WatchHistoryRepository watchHistoryRepository) {
    return new SeriesDetailViewModel(iptvRepository, favoriteRepository, watchHistoryRepository);
  }
}
