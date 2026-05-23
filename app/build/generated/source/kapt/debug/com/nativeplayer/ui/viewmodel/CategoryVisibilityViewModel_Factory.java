package com.nativeplayer.ui.viewmodel;

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
public final class CategoryVisibilityViewModel_Factory implements Factory<CategoryVisibilityViewModel> {
  private final Provider<PreferencesManager> preferencesManagerProvider;

  private final Provider<IptvRepository> repositoryProvider;

  public CategoryVisibilityViewModel_Factory(
      Provider<PreferencesManager> preferencesManagerProvider,
      Provider<IptvRepository> repositoryProvider) {
    this.preferencesManagerProvider = preferencesManagerProvider;
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public CategoryVisibilityViewModel get() {
    return newInstance(preferencesManagerProvider.get(), repositoryProvider.get());
  }

  public static CategoryVisibilityViewModel_Factory create(
      Provider<PreferencesManager> preferencesManagerProvider,
      Provider<IptvRepository> repositoryProvider) {
    return new CategoryVisibilityViewModel_Factory(preferencesManagerProvider, repositoryProvider);
  }

  public static CategoryVisibilityViewModel newInstance(PreferencesManager preferencesManager,
      IptvRepository repository) {
    return new CategoryVisibilityViewModel(preferencesManager, repository);
  }
}
