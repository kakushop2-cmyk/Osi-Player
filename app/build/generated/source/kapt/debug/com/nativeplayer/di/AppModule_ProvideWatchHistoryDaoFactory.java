package com.nativeplayer.di;

import com.nativeplayer.data.repository.AppDatabase;
import com.nativeplayer.data.repository.WatchHistoryDao;
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
public final class AppModule_ProvideWatchHistoryDaoFactory implements Factory<WatchHistoryDao> {
  private final Provider<AppDatabase> dbProvider;

  public AppModule_ProvideWatchHistoryDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public WatchHistoryDao get() {
    return provideWatchHistoryDao(dbProvider.get());
  }

  public static AppModule_ProvideWatchHistoryDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new AppModule_ProvideWatchHistoryDaoFactory(dbProvider);
  }

  public static WatchHistoryDao provideWatchHistoryDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideWatchHistoryDao(db));
  }
}
