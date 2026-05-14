package com.nativeplayer.ui.live;

import com.nativeplayer.data.repository.FavoriteRepository;
import com.nativeplayer.data.repository.IptvRepository;
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

  public LiveViewModel_Factory(Provider<IptvRepository> repositoryProvider,
      Provider<FavoriteRepository> favoriteRepositoryProvider) {
    this.repositoryProvider = repositoryProvider;
    this.favoriteRepositoryProvider = favoriteRepositoryProvider;
  }

  @Override
  public LiveViewModel get() {
    return newInstance(repositoryProvider.get(), favoriteRepositoryProvider.get());
  }

  public static LiveViewModel_Factory create(Provider<IptvRepository> repositoryProvider,
      Provider<FavoriteRepository> favoriteRepositoryProvider) {
    return new LiveViewModel_Factory(repositoryProvider, favoriteRepositoryProvider);
  }

  public static LiveViewModel newInstance(IptvRepository repository,
      FavoriteRepository favoriteRepository) {
    return new LiveViewModel(repository, favoriteRepository);
  }
}
