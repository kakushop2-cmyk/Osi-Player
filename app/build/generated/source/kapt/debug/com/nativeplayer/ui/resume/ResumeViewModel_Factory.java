package com.nativeplayer.ui.resume;

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
public final class ResumeViewModel_Factory implements Factory<ResumeViewModel> {
  private final Provider<WatchHistoryRepository> watchHistoryRepositoryProvider;

  public ResumeViewModel_Factory(Provider<WatchHistoryRepository> watchHistoryRepositoryProvider) {
    this.watchHistoryRepositoryProvider = watchHistoryRepositoryProvider;
  }

  @Override
  public ResumeViewModel get() {
    return newInstance(watchHistoryRepositoryProvider.get());
  }

  public static ResumeViewModel_Factory create(
      Provider<WatchHistoryRepository> watchHistoryRepositoryProvider) {
    return new ResumeViewModel_Factory(watchHistoryRepositoryProvider);
  }

  public static ResumeViewModel newInstance(WatchHistoryRepository watchHistoryRepository) {
    return new ResumeViewModel(watchHistoryRepository);
  }
}
