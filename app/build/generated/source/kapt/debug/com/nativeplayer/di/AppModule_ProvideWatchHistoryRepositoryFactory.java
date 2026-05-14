package com.nativeplayer.di;

import com.nativeplayer.data.repository.WatchHistoryDao;
import com.nativeplayer.data.repository.WatchHistoryRepository;
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
public final class AppModule_ProvideWatchHistoryRepositoryFactory implements Factory<WatchHistoryRepository> {
  private final Provider<WatchHistoryDao> daoProvider;

  public AppModule_ProvideWatchHistoryRepositoryFactory(Provider<WatchHistoryDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public WatchHistoryRepository get() {
    return provideWatchHistoryRepository(daoProvider.get());
  }

  public static AppModule_ProvideWatchHistoryRepositoryFactory create(
      Provider<WatchHistoryDao> daoProvider) {
    return new AppModule_ProvideWatchHistoryRepositoryFactory(daoProvider);
  }

  public static WatchHistoryRepository provideWatchHistoryRepository(WatchHistoryDao dao) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideWatchHistoryRepository(dao));
  }
}
