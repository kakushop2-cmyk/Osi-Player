package com.nativeplayer.di;

import com.nativeplayer.data.repository.PreferencesManager;
import com.nativeplayer.ui.viewmodel.CategoryVisibilityViewModel;
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
public final class AppModule_ProvideCategoryVisibilityViewModelFactory implements Factory<CategoryVisibilityViewModel> {
  private final Provider<PreferencesManager> preferencesManagerProvider;

  public AppModule_ProvideCategoryVisibilityViewModelFactory(
      Provider<PreferencesManager> preferencesManagerProvider) {
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public CategoryVisibilityViewModel get() {
    return provideCategoryVisibilityViewModel(preferencesManagerProvider.get());
  }

  public static AppModule_ProvideCategoryVisibilityViewModelFactory create(
      Provider<PreferencesManager> preferencesManagerProvider) {
    return new AppModule_ProvideCategoryVisibilityViewModelFactory(preferencesManagerProvider);
  }

  public static CategoryVisibilityViewModel provideCategoryVisibilityViewModel(
      PreferencesManager preferencesManager) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideCategoryVisibilityViewModel(preferencesManager));
  }
}
