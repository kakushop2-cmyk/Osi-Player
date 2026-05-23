package com.nativeplayer.ui.home;

import android.app.Application;
import com.nativeplayer.data.repository.IptvRepository;
import com.nativeplayer.data.repository.WatchHistoryRepository;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<WatchHistoryRepository> watchHistoryRepositoryProvider;

  private final Provider<IptvRepository> iptvRepositoryProvider;

  private final Provider<Application> applicationProvider;

  public HomeViewModel_Factory(Provider<WatchHistoryRepository> watchHistoryRepositoryProvider,
      Provider<IptvRepository> iptvRepositoryProvider, Provider<Application> applicationProvider) {
    this.watchHistoryRepositoryProvider = watchHistoryRepositoryProvider;
    this.iptvRepositoryProvider = iptvRepositoryProvider;
    this.applicationProvider = applicationProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(watchHistoryRepositoryProvider.get(), iptvRepositoryProvider.get(), applicationProvider.get());
  }

  public static HomeViewModel_Factory create(
      Provider<WatchHistoryRepository> watchHistoryRepositoryProvider,
      Provider<IptvRepository> iptvRepositoryProvider, Provider<Application> applicationProvider) {
    return new HomeViewModel_Factory(watchHistoryRepositoryProvider, iptvRepositoryProvider, applicationProvider);
  }

  public static HomeViewModel newInstance(WatchHistoryRepository watchHistoryRepository,
      IptvRepository iptvRepository, Application application) {
    return new HomeViewModel(watchHistoryRepository, iptvRepository, application);
  }
}
