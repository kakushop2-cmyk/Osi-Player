package com.nativeplayer.di;

import com.nativeplayer.data.repository.FavoriteDao;
import com.nativeplayer.data.repository.FavoriteRepository;
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
public final class AppModule_ProvideFavoriteRepositoryFactory implements Factory<FavoriteRepository> {
  private final Provider<FavoriteDao> daoProvider;

  public AppModule_ProvideFavoriteRepositoryFactory(Provider<FavoriteDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public FavoriteRepository get() {
    return provideFavoriteRepository(daoProvider.get());
  }

  public static AppModule_ProvideFavoriteRepositoryFactory create(
      Provider<FavoriteDao> daoProvider) {
    return new AppModule_ProvideFavoriteRepositoryFactory(daoProvider);
  }

  public static FavoriteRepository provideFavoriteRepository(FavoriteDao dao) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideFavoriteRepository(dao));
  }
}
