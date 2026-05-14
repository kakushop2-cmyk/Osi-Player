package com.nativeplayer.di;

import com.nativeplayer.data.repository.IptvRepository;
import com.nativeplayer.data.repository.PreferencesManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideIptvRepositoryFactory implements Factory<IptvRepository> {
  private final Provider<OkHttpClient> okHttpClientProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  public AppModule_ProvideIptvRepositoryFactory(Provider<OkHttpClient> okHttpClientProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    this.okHttpClientProvider = okHttpClientProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public IptvRepository get() {
    return provideIptvRepository(okHttpClientProvider.get(), preferencesManagerProvider.get());
  }

  public static AppModule_ProvideIptvRepositoryFactory create(
      Provider<OkHttpClient> okHttpClientProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    return new AppModule_ProvideIptvRepositoryFactory(okHttpClientProvider, preferencesManagerProvider);
  }

  public static IptvRepository provideIptvRepository(OkHttpClient okHttpClient,
      PreferencesManager preferencesManager) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideIptvRepository(okHttpClient, preferencesManager));
  }
}
