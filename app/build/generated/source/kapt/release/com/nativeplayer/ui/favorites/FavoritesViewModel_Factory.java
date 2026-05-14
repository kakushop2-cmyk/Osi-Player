package com.nativeplayer.ui.favorites;

import com.nativeplayer.data.repository.FavoriteRepository;
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
public final class FavoritesViewModel_Factory implements Factory<FavoritesViewModel> {
  private final Provider<FavoriteRepository> favoriteRepositoryProvider;

  public FavoritesViewModel_Factory(Provider<FavoriteRepository> favoriteRepositoryProvider) {
    this.favoriteRepositoryProvider = favoriteRepositoryProvider;
  }

  @Override
  public FavoritesViewModel get() {
    return newInstance(favoriteRepositoryProvider.get());
  }

  public static FavoritesViewModel_Factory create(
      Provider<FavoriteRepository> favoriteRepositoryProvider) {
    return new FavoritesViewModel_Factory(favoriteRepositoryProvider);
  }

  public static FavoritesViewModel newInstance(FavoriteRepository favoriteRepository) {
    return new FavoritesViewModel(favoriteRepository);
  }
}
