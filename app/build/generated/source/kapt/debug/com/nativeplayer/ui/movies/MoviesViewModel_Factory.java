package com.nativeplayer.ui.movies;

import com.nativeplayer.data.repository.FavoriteRepository;
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
public final class MoviesViewModel_Factory implements Factory<MoviesViewModel> {
  private final Provider<IptvRepository> repositoryProvider;

  private final Provider<FavoriteRepository> favoriteRepositoryProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  public MoviesViewModel_Factory(Provider<IptvRepository> repositoryProvider,
      Provider<FavoriteRepository> favoriteRepositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    this.repositoryProvider = repositoryProvider;
    this.favoriteRepositoryProvider = favoriteRepositoryProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public MoviesViewModel get() {
    return newInstance(repositoryProvider.get(), favoriteRepositoryProvider.get(), preferencesManagerProvider.get());
  }

  public static MoviesViewModel_Factory create(Provider<IptvRepository> repositoryProvider,
      Provider<FavoriteRepository> favoriteRepositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    return new MoviesViewModel_Factory(repositoryProvider, favoriteRepositoryProvider, preferencesManagerProvider);
  }

  public static MoviesViewModel newInstance(IptvRepository repository,
      FavoriteRepository favoriteRepository, PreferencesManager preferencesManager) {
    return new MoviesViewModel(repository, favoriteRepository, preferencesManager);
  }
}
