package com.nativeplayer.ui.movies;

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
public final class MovieDetailViewModel_Factory implements Factory<MovieDetailViewModel> {
  private final Provider<FavoriteRepository> favoriteRepositoryProvider;

  private final Provider<WatchHistoryRepository> watchHistoryRepositoryProvider;

  private final Provider<IptvRepository> iptvRepositoryProvider;

  public MovieDetailViewModel_Factory(Provider<FavoriteRepository> favoriteRepositoryProvider,
      Provider<WatchHistoryRepository> watchHistoryRepositoryProvider,
      Provider<IptvRepository> iptvRepositoryProvider) {
    this.favoriteRepositoryProvider = favoriteRepositoryProvider;
    this.watchHistoryRepositoryProvider = watchHistoryRepositoryProvider;
    this.iptvRepositoryProvider = iptvRepositoryProvider;
  }

  @Override
  public MovieDetailViewModel get() {
    return newInstance(favoriteRepositoryProvider.get(), watchHistoryRepositoryProvider.get(), iptvRepositoryProvider.get());
  }

  public static MovieDetailViewModel_Factory create(
      Provider<FavoriteRepository> favoriteRepositoryProvider,
      Provider<WatchHistoryRepository> watchHistoryRepositoryProvider,
      Provider<IptvRepository> iptvRepositoryProvider) {
    return new MovieDetailViewModel_Factory(favoriteRepositoryProvider, watchHistoryRepositoryProvider, iptvRepositoryProvider);
  }

  public static MovieDetailViewModel newInstance(FavoriteRepository favoriteRepository,
      WatchHistoryRepository watchHistoryRepository, IptvRepository iptvRepository) {
    return new MovieDetailViewModel(favoriteRepository, watchHistoryRepository, iptvRepository);
  }
}
