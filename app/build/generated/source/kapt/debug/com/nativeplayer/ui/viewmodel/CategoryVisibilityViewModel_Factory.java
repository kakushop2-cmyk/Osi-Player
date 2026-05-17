package com.nativeplayer.ui.viewmodel;

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

  public CategoryVisibilityViewModel_Factory(
      Provider<PreferencesManager> preferencesManagerProvider) {
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public CategoryVisibilityViewModel get() {
    return newInstance(preferencesManagerProvider.get());
  }

  public static CategoryVisibilityViewModel_Factory create(
      Provider<PreferencesManager> preferencesManagerProvider) {
    return new CategoryVisibilityViewModel_Factory(preferencesManagerProvider);
  }

  public static CategoryVisibilityViewModel newInstance(PreferencesManager preferencesManager) {
    return new CategoryVisibilityViewModel(preferencesManager);
  }
}
