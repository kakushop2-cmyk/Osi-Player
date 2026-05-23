package com.nativeplayer.ui.login;

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
public final class LoginViewModel_Factory implements Factory<LoginViewModel> {
  private final Provider<IptvRepository> repositoryProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  public LoginViewModel_Factory(Provider<IptvRepository> repositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    this.repositoryProvider = repositoryProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public LoginViewModel get() {
    return newInstance(repositoryProvider.get(), preferencesManagerProvider.get());
  }

  public static LoginViewModel_Factory create(Provider<IptvRepository> repositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    return new LoginViewModel_Factory(repositoryProvider, preferencesManagerProvider);
  }

  public static LoginViewModel newInstance(IptvRepository repository,
      PreferencesManager preferencesManager) {
    return new LoginViewModel(repository, preferencesManager);
  }
}
