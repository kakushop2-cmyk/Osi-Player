package com.nativeplayer.ui.live;

import com.nativeplayer.data.repository.FavoriteRepository;
import com.nativeplayer.data.repository.IptvRepository;
import com.nativeplayer.data.repository.PreferencesManager;
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
public final class LiveViewModel_Factory implements Factory<LiveViewModel> {
  private final Provider<IptvRepository> repositoryProvider;

  private final Provider<FavoriteRepository> favoriteRepositoryProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  private final Provider<CategoryVisibilityViewModel> categoryVisibilityViewModelProvider;

  public LiveViewModel_Factory(Provider<IptvRepository> repositoryProvider,
      Provider<FavoriteRepository> favoriteRepositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider,
      Provider<CategoryVisibilityViewModel> categoryVisibilityViewModelProvider) {
    this.repositoryProvider = repositoryProvider;
    this.favoriteRepositoryProvider = favoriteRepositoryProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
    this.categoryVisibilityViewModelProvider = categoryVisibilityViewModelProvider;
  }

  @Override
  public LiveViewModel get() {
    return newInstance(repositoryProvider.get(), favoriteRepositoryProvider.get(), preferencesManagerProvider.get(), categoryVisibilityViewModelProvider.get());
  }

  public static LiveViewModel_Factory create(Provider<IptvRepository> repositoryProvider,
      Provider<FavoriteRepository> favoriteRepositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider,
      Provider<CategoryVisibilityViewModel> categoryVisibilityViewModelProvider) {
    return new LiveViewModel_Factory(repositoryProvider, favoriteRepositoryProvider, preferencesManagerProvider, categoryVisibilityViewModelProvider);
  }

  public static LiveViewModel newInstance(IptvRepository repository,
      FavoriteRepository favoriteRepository, PreferencesManager preferencesManager,
      CategoryVisibilityViewModel categoryVisibilityViewModel) {
    return new LiveViewModel(repository, favoriteRepository, preferencesManager, categoryVisibilityViewModel);
  }
}
