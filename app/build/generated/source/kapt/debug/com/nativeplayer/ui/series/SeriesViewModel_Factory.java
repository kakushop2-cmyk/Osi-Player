package com.nativeplayer.ui.series;

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
public final class SeriesViewModel_Factory implements Factory<SeriesViewModel> {
  private final Provider<IptvRepository> repositoryProvider;

  private final Provider<FavoriteRepository> favoriteRepositoryProvider;

  private final Provider<CategoryVisibilityViewModel> categoryVisibilityViewModelProvider;

  public SeriesViewModel_Factory(Provider<IptvRepository> repositoryProvider,
      Provider<FavoriteRepository> favoriteRepositoryProvider,
      Provider<CategoryVisibilityViewModel> categoryVisibilityViewModelProvider) {
    this.repositoryProvider = repositoryProvider;
    this.favoriteRepositoryProvider = favoriteRepositoryProvider;
    this.categoryVisibilityViewModelProvider = categoryVisibilityViewModelProvider;
  }

  @Override
  public SeriesViewModel get() {
    return newInstance(repositoryProvider.get(), favoriteRepositoryProvider.get(), categoryVisibilityViewModelProvider.get());
  }

  public static SeriesViewModel_Factory create(Provider<IptvRepository> repositoryProvider,
      Provider<FavoriteRepository> favoriteRepositoryProvider,
      Provider<CategoryVisibilityViewModel> categoryVisibilityViewModelProvider) {
    return new SeriesViewModel_Factory(repositoryProvider, favoriteRepositoryProvider, categoryVisibilityViewModelProvider);
  }

  public static SeriesViewModel newInstance(IptvRepository repository,
      FavoriteRepository favoriteRepository,
      CategoryVisibilityViewModel categoryVisibilityViewModel) {
    return new SeriesViewModel(repository, favoriteRepository, categoryVisibilityViewModel);
  }
}
