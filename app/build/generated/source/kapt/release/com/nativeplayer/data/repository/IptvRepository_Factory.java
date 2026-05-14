package com.nativeplayer.data.repository;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;

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
public final class IptvRepository_Factory implements Factory<IptvRepository> {
  private final Provider<OkHttpClient> okHttpClientProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  public IptvRepository_Factory(Provider<OkHttpClient> okHttpClientProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    this.okHttpClientProvider = okHttpClientProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public IptvRepository get() {
    return newInstance(okHttpClientProvider.get(), preferencesManagerProvider.get());
  }

  public static IptvRepository_Factory create(Provider<OkHttpClient> okHttpClientProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    return new IptvRepository_Factory(okHttpClientProvider, preferencesManagerProvider);
  }

  public static IptvRepository newInstance(OkHttpClient okHttpClient,
      PreferencesManager preferencesManager) {
    return new IptvRepository(okHttpClient, preferencesManager);
  }
}
