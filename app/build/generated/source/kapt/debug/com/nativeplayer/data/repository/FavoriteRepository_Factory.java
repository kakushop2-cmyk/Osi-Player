package com.nativeplayer.data.repository;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class FavoriteRepository_Factory implements Factory<FavoriteRepository> {
  private final Provider<FavoriteDao> favoriteDaoProvider;

  public FavoriteRepository_Factory(Provider<FavoriteDao> favoriteDaoProvider) {
    this.favoriteDaoProvider = favoriteDaoProvider;
  }

  @Override
  public FavoriteRepository get() {
    return newInstance(favoriteDaoProvider.get());
  }

  public static FavoriteRepository_Factory create(Provider<FavoriteDao> favoriteDaoProvider) {
    return new FavoriteRepository_Factory(favoriteDaoProvider);
  }

  public static FavoriteRepository newInstance(FavoriteDao favoriteDao) {
    return new FavoriteRepository(favoriteDao);
  }
}
