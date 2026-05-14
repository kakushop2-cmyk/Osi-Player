package com.nativeplayer.di;

import com.nativeplayer.data.repository.AppDatabase;
import com.nativeplayer.data.repository.FavoriteDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideFavoriteDaoFactory implements Factory<FavoriteDao> {
  private final Provider<AppDatabase> dbProvider;

  public AppModule_ProvideFavoriteDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public FavoriteDao get() {
    return provideFavoriteDao(dbProvider.get());
  }

  public static AppModule_ProvideFavoriteDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new AppModule_ProvideFavoriteDaoFactory(dbProvider);
  }

  public static FavoriteDao provideFavoriteDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideFavoriteDao(db));
  }
}
