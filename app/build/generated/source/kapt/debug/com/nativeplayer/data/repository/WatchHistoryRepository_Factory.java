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
public final class WatchHistoryRepository_Factory implements Factory<WatchHistoryRepository> {
  private final Provider<WatchHistoryDao> watchHistoryDaoProvider;

  public WatchHistoryRepository_Factory(Provider<WatchHistoryDao> watchHistoryDaoProvider) {
    this.watchHistoryDaoProvider = watchHistoryDaoProvider;
  }

  @Override
  public WatchHistoryRepository get() {
    return newInstance(watchHistoryDaoProvider.get());
  }

  public static WatchHistoryRepository_Factory create(
      Provider<WatchHistoryDao> watchHistoryDaoProvider) {
    return new WatchHistoryRepository_Factory(watchHistoryDaoProvider);
  }

  public static WatchHistoryRepository newInstance(WatchHistoryDao watchHistoryDao) {
    return new WatchHistoryRepository(watchHistoryDao);
  }
}
