package com.nativeplayer.ui.splash;

import com.nativeplayer.data.repository.IptvRepository;
import com.nativeplayer.data.repository.PreferencesManager;
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
public final class IntroViewModel_Factory implements Factory<IntroViewModel> {
  private final Provider<IptvRepository> repositoryProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  public IntroViewModel_Factory(Provider<IptvRepository> repositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    this.repositoryProvider = repositoryProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public IntroViewModel get() {
    return newInstance(repositoryProvider.get(), preferencesManagerProvider.get());
  }

  public static IntroViewModel_Factory create(Provider<IptvRepository> repositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    return new IntroViewModel_Factory(repositoryProvider, preferencesManagerProvider);
  }

  public static IntroViewModel newInstance(IptvRepository repository,
      PreferencesManager preferencesManager) {
    return new IntroViewModel(repository, preferencesManager);
  }
}
