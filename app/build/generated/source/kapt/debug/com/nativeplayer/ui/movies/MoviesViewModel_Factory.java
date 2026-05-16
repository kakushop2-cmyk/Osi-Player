package com.nativeplayer.ui.movies;

import com.nativeplayer.data.repository.FavoriteRepository;
import com.nativeplayer.data.repository.IptvRepository;
import com.nativeplayer.ui.viewmodel.CategoryVisibilityViewModel;
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

  private final Provider<CategoryVisibilityViewModel> categoryVisibilityViewModelProvider;

  public MoviesViewModel_Factory(Provider<IptvRepository> repositoryProvider,
      Provider<FavoriteRepository> favoriteRepositoryProvider,
      Provider<CategoryVisibilityViewModel> categoryVisibilityViewModelProvider) {
    this.repositoryProvider = repositoryProvider;
    this.favoriteRepositoryProvider = favoriteRepositoryProvider;
    this.categoryVisibilityViewModelProvider = categoryVisibilityViewModelProvider;
  }

  @Override
  public MoviesViewModel get() {
    return newInstance(repositoryProvider.get(), favoriteRepositoryProvider.get(), categoryVisibilityViewModelProvider.get());
  }

  public static MoviesViewModel_Factory create(Provider<IptvRepository> repositoryProvider,
      Provider<FavoriteRepository> favoriteRepositoryProvider,
      Provider<CategoryVisibilityViewModel> categoryVisibilityViewModelProvider) {
    return new MoviesViewModel_Factory(repositoryProvider, favoriteRepositoryProvider, categoryVisibilityViewModelProvider);
  }

  public static MoviesViewModel newInstance(IptvRepository repository,
      FavoriteRepository favoriteRepository,
      CategoryVisibilityViewModel categoryVisibilityViewModel) {
    return new MoviesViewModel(repository, favoriteRepository, categoryVisibilityViewModel);
  }
}
