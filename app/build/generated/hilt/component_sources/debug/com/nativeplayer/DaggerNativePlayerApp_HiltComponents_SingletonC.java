package com.nativeplayer;

import android.app.Activity;
import android.app.Service;
import android.content.SharedPreferences;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.nativeplayer.data.repository.AppDatabase;
import com.nativeplayer.data.repository.FavoriteDao;
import com.nativeplayer.data.repository.FavoriteRepository;
import com.nativeplayer.data.repository.IptvRepository;
import com.nativeplayer.data.repository.PreferencesManager;
import com.nativeplayer.data.repository.WatchHistoryDao;
import com.nativeplayer.data.repository.WatchHistoryRepository;
import com.nativeplayer.di.AppModule_ProvideAppDatabaseFactory;
import com.nativeplayer.di.AppModule_ProvideFavoriteDaoFactory;
import com.nativeplayer.di.AppModule_ProvideFavoriteRepositoryFactory;
import com.nativeplayer.di.AppModule_ProvideIptvRepositoryFactory;
import com.nativeplayer.di.AppModule_ProvideOkHttpClientFactory;
import com.nativeplayer.di.AppModule_ProvidePreferencesManagerFactory;
import com.nativeplayer.di.AppModule_ProvideSharedPreferencesFactory;
import com.nativeplayer.di.AppModule_ProvideWatchHistoryDaoFactory;
import com.nativeplayer.di.AppModule_ProvideWatchHistoryRepositoryFactory;
import com.nativeplayer.ui.favorites.FavoritesViewModel;
import com.nativeplayer.ui.favorites.FavoritesViewModel_HiltModules;
import com.nativeplayer.ui.home.HomeViewModel;
import com.nativeplayer.ui.home.HomeViewModel_HiltModules;
import com.nativeplayer.ui.live.LiveViewModel;
import com.nativeplayer.ui.live.LiveViewModel_HiltModules;
import com.nativeplayer.ui.login.LoginViewModel;
import com.nativeplayer.ui.login.LoginViewModel_HiltModules;
import com.nativeplayer.ui.movies.MovieDetailViewModel;
import com.nativeplayer.ui.movies.MovieDetailViewModel_HiltModules;
import com.nativeplayer.ui.movies.MoviesViewModel;
import com.nativeplayer.ui.movies.MoviesViewModel_HiltModules;
import com.nativeplayer.ui.player.PlayerViewModel;
import com.nativeplayer.ui.player.PlayerViewModel_HiltModules;
import com.nativeplayer.ui.resume.ResumeViewModel;
import com.nativeplayer.ui.resume.ResumeViewModel_HiltModules;
import com.nativeplayer.ui.search.SearchViewModel;
import com.nativeplayer.ui.search.SearchViewModel_HiltModules;
import com.nativeplayer.ui.series.SeriesDetailViewModel;
import com.nativeplayer.ui.series.SeriesDetailViewModel_HiltModules;
import com.nativeplayer.ui.series.SeriesViewModel;
import com.nativeplayer.ui.series.SeriesViewModel_HiltModules;
import com.nativeplayer.ui.settings.PlayerSettingsViewModel;
import com.nativeplayer.ui.settings.PlayerSettingsViewModel_HiltModules;
import com.nativeplayer.ui.splash.IntroViewModel;
import com.nativeplayer.ui.splash.IntroViewModel_HiltModules;
import com.nativeplayer.ui.splash.SplashViewModel;
import com.nativeplayer.ui.splash.SplashViewModel_HiltModules;
import com.nativeplayer.ui.viewmodel.CategoryVisibilityViewModel;
import com.nativeplayer.ui.viewmodel.CategoryVisibilityViewModel_HiltModules;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideApplicationFactory;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;
import okhttp3.OkHttpClient;

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
public final class DaggerNativePlayerApp_HiltComponents_SingletonC {
  private DaggerNativePlayerApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public NativePlayerApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements NativePlayerApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public NativePlayerApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements NativePlayerApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public NativePlayerApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements NativePlayerApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public NativePlayerApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements NativePlayerApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public NativePlayerApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements NativePlayerApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public NativePlayerApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements NativePlayerApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public NativePlayerApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements NativePlayerApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public NativePlayerApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends NativePlayerApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends NativePlayerApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends NativePlayerApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends NativePlayerApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(ImmutableMap.<String, Boolean>builderWithExpectedSize(15).put(LazyClassKeyProvider.com_nativeplayer_ui_viewmodel_CategoryVisibilityViewModel, CategoryVisibilityViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_nativeplayer_ui_favorites_FavoritesViewModel, FavoritesViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_nativeplayer_ui_home_HomeViewModel, HomeViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_nativeplayer_ui_splash_IntroViewModel, IntroViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_nativeplayer_ui_live_LiveViewModel, LiveViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_nativeplayer_ui_login_LoginViewModel, LoginViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_nativeplayer_ui_movies_MovieDetailViewModel, MovieDetailViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_nativeplayer_ui_movies_MoviesViewModel, MoviesViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_nativeplayer_ui_settings_PlayerSettingsViewModel, PlayerSettingsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_nativeplayer_ui_player_PlayerViewModel, PlayerViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_nativeplayer_ui_resume_ResumeViewModel, ResumeViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_nativeplayer_ui_search_SearchViewModel, SearchViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_nativeplayer_ui_series_SeriesDetailViewModel, SeriesDetailViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_nativeplayer_ui_series_SeriesViewModel, SeriesViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_nativeplayer_ui_splash_SplashViewModel, SplashViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_nativeplayer_ui_player_PlayerViewModel = "com.nativeplayer.ui.player.PlayerViewModel";

      static String com_nativeplayer_ui_resume_ResumeViewModel = "com.nativeplayer.ui.resume.ResumeViewModel";

      static String com_nativeplayer_ui_splash_SplashViewModel = "com.nativeplayer.ui.splash.SplashViewModel";

      static String com_nativeplayer_ui_viewmodel_CategoryVisibilityViewModel = "com.nativeplayer.ui.viewmodel.CategoryVisibilityViewModel";

      static String com_nativeplayer_ui_movies_MoviesViewModel = "com.nativeplayer.ui.movies.MoviesViewModel";

      static String com_nativeplayer_ui_splash_IntroViewModel = "com.nativeplayer.ui.splash.IntroViewModel";

      static String com_nativeplayer_ui_search_SearchViewModel = "com.nativeplayer.ui.search.SearchViewModel";

      static String com_nativeplayer_ui_live_LiveViewModel = "com.nativeplayer.ui.live.LiveViewModel";

      static String com_nativeplayer_ui_settings_PlayerSettingsViewModel = "com.nativeplayer.ui.settings.PlayerSettingsViewModel";

      static String com_nativeplayer_ui_favorites_FavoritesViewModel = "com.nativeplayer.ui.favorites.FavoritesViewModel";

      static String com_nativeplayer_ui_home_HomeViewModel = "com.nativeplayer.ui.home.HomeViewModel";

      static String com_nativeplayer_ui_series_SeriesViewModel = "com.nativeplayer.ui.series.SeriesViewModel";

      static String com_nativeplayer_ui_series_SeriesDetailViewModel = "com.nativeplayer.ui.series.SeriesDetailViewModel";

      static String com_nativeplayer_ui_login_LoginViewModel = "com.nativeplayer.ui.login.LoginViewModel";

      static String com_nativeplayer_ui_movies_MovieDetailViewModel = "com.nativeplayer.ui.movies.MovieDetailViewModel";

      @KeepFieldType
      PlayerViewModel com_nativeplayer_ui_player_PlayerViewModel2;

      @KeepFieldType
      ResumeViewModel com_nativeplayer_ui_resume_ResumeViewModel2;

      @KeepFieldType
      SplashViewModel com_nativeplayer_ui_splash_SplashViewModel2;

      @KeepFieldType
      CategoryVisibilityViewModel com_nativeplayer_ui_viewmodel_CategoryVisibilityViewModel2;

      @KeepFieldType
      MoviesViewModel com_nativeplayer_ui_movies_MoviesViewModel2;

      @KeepFieldType
      IntroViewModel com_nativeplayer_ui_splash_IntroViewModel2;

      @KeepFieldType
      SearchViewModel com_nativeplayer_ui_search_SearchViewModel2;

      @KeepFieldType
      LiveViewModel com_nativeplayer_ui_live_LiveViewModel2;

      @KeepFieldType
      PlayerSettingsViewModel com_nativeplayer_ui_settings_PlayerSettingsViewModel2;

      @KeepFieldType
      FavoritesViewModel com_nativeplayer_ui_favorites_FavoritesViewModel2;

      @KeepFieldType
      HomeViewModel com_nativeplayer_ui_home_HomeViewModel2;

      @KeepFieldType
      SeriesViewModel com_nativeplayer_ui_series_SeriesViewModel2;

      @KeepFieldType
      SeriesDetailViewModel com_nativeplayer_ui_series_SeriesDetailViewModel2;

      @KeepFieldType
      LoginViewModel com_nativeplayer_ui_login_LoginViewModel2;

      @KeepFieldType
      MovieDetailViewModel com_nativeplayer_ui_movies_MovieDetailViewModel2;
    }
  }

  private static final class ViewModelCImpl extends NativePlayerApp_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<CategoryVisibilityViewModel> categoryVisibilityViewModelProvider;

    private Provider<FavoritesViewModel> favoritesViewModelProvider;

    private Provider<HomeViewModel> homeViewModelProvider;

    private Provider<IntroViewModel> introViewModelProvider;

    private Provider<LiveViewModel> liveViewModelProvider;

    private Provider<LoginViewModel> loginViewModelProvider;

    private Provider<MovieDetailViewModel> movieDetailViewModelProvider;

    private Provider<MoviesViewModel> moviesViewModelProvider;

    private Provider<PlayerSettingsViewModel> playerSettingsViewModelProvider;

    private Provider<PlayerViewModel> playerViewModelProvider;

    private Provider<ResumeViewModel> resumeViewModelProvider;

    private Provider<SearchViewModel> searchViewModelProvider;

    private Provider<SeriesDetailViewModel> seriesDetailViewModelProvider;

    private Provider<SeriesViewModel> seriesViewModelProvider;

    private Provider<SplashViewModel> splashViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.categoryVisibilityViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.favoritesViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.homeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.introViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.liveViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.loginViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.movieDetailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.moviesViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
      this.playerSettingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 8);
      this.playerViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 9);
      this.resumeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 10);
      this.searchViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 11);
      this.seriesDetailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 12);
      this.seriesViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 13);
      this.splashViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 14);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(ImmutableMap.<String, javax.inject.Provider<ViewModel>>builderWithExpectedSize(15).put(LazyClassKeyProvider.com_nativeplayer_ui_viewmodel_CategoryVisibilityViewModel, ((Provider) categoryVisibilityViewModelProvider)).put(LazyClassKeyProvider.com_nativeplayer_ui_favorites_FavoritesViewModel, ((Provider) favoritesViewModelProvider)).put(LazyClassKeyProvider.com_nativeplayer_ui_home_HomeViewModel, ((Provider) homeViewModelProvider)).put(LazyClassKeyProvider.com_nativeplayer_ui_splash_IntroViewModel, ((Provider) introViewModelProvider)).put(LazyClassKeyProvider.com_nativeplayer_ui_live_LiveViewModel, ((Provider) liveViewModelProvider)).put(LazyClassKeyProvider.com_nativeplayer_ui_login_LoginViewModel, ((Provider) loginViewModelProvider)).put(LazyClassKeyProvider.com_nativeplayer_ui_movies_MovieDetailViewModel, ((Provider) movieDetailViewModelProvider)).put(LazyClassKeyProvider.com_nativeplayer_ui_movies_MoviesViewModel, ((Provider) moviesViewModelProvider)).put(LazyClassKeyProvider.com_nativeplayer_ui_settings_PlayerSettingsViewModel, ((Provider) playerSettingsViewModelProvider)).put(LazyClassKeyProvider.com_nativeplayer_ui_player_PlayerViewModel, ((Provider) playerViewModelProvider)).put(LazyClassKeyProvider.com_nativeplayer_ui_resume_ResumeViewModel, ((Provider) resumeViewModelProvider)).put(LazyClassKeyProvider.com_nativeplayer_ui_search_SearchViewModel, ((Provider) searchViewModelProvider)).put(LazyClassKeyProvider.com_nativeplayer_ui_series_SeriesDetailViewModel, ((Provider) seriesDetailViewModelProvider)).put(LazyClassKeyProvider.com_nativeplayer_ui_series_SeriesViewModel, ((Provider) seriesViewModelProvider)).put(LazyClassKeyProvider.com_nativeplayer_ui_splash_SplashViewModel, ((Provider) splashViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return ImmutableMap.<Class<?>, Object>of();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_nativeplayer_ui_live_LiveViewModel = "com.nativeplayer.ui.live.LiveViewModel";

      static String com_nativeplayer_ui_favorites_FavoritesViewModel = "com.nativeplayer.ui.favorites.FavoritesViewModel";

      static String com_nativeplayer_ui_home_HomeViewModel = "com.nativeplayer.ui.home.HomeViewModel";

      static String com_nativeplayer_ui_search_SearchViewModel = "com.nativeplayer.ui.search.SearchViewModel";

      static String com_nativeplayer_ui_movies_MovieDetailViewModel = "com.nativeplayer.ui.movies.MovieDetailViewModel";

      static String com_nativeplayer_ui_movies_MoviesViewModel = "com.nativeplayer.ui.movies.MoviesViewModel";

      static String com_nativeplayer_ui_splash_IntroViewModel = "com.nativeplayer.ui.splash.IntroViewModel";

      static String com_nativeplayer_ui_resume_ResumeViewModel = "com.nativeplayer.ui.resume.ResumeViewModel";

      static String com_nativeplayer_ui_series_SeriesViewModel = "com.nativeplayer.ui.series.SeriesViewModel";

      static String com_nativeplayer_ui_viewmodel_CategoryVisibilityViewModel = "com.nativeplayer.ui.viewmodel.CategoryVisibilityViewModel";

      static String com_nativeplayer_ui_splash_SplashViewModel = "com.nativeplayer.ui.splash.SplashViewModel";

      static String com_nativeplayer_ui_settings_PlayerSettingsViewModel = "com.nativeplayer.ui.settings.PlayerSettingsViewModel";

      static String com_nativeplayer_ui_player_PlayerViewModel = "com.nativeplayer.ui.player.PlayerViewModel";

      static String com_nativeplayer_ui_login_LoginViewModel = "com.nativeplayer.ui.login.LoginViewModel";

      static String com_nativeplayer_ui_series_SeriesDetailViewModel = "com.nativeplayer.ui.series.SeriesDetailViewModel";

      @KeepFieldType
      LiveViewModel com_nativeplayer_ui_live_LiveViewModel2;

      @KeepFieldType
      FavoritesViewModel com_nativeplayer_ui_favorites_FavoritesViewModel2;

      @KeepFieldType
      HomeViewModel com_nativeplayer_ui_home_HomeViewModel2;

      @KeepFieldType
      SearchViewModel com_nativeplayer_ui_search_SearchViewModel2;

      @KeepFieldType
      MovieDetailViewModel com_nativeplayer_ui_movies_MovieDetailViewModel2;

      @KeepFieldType
      MoviesViewModel com_nativeplayer_ui_movies_MoviesViewModel2;

      @KeepFieldType
      IntroViewModel com_nativeplayer_ui_splash_IntroViewModel2;

      @KeepFieldType
      ResumeViewModel com_nativeplayer_ui_resume_ResumeViewModel2;

      @KeepFieldType
      SeriesViewModel com_nativeplayer_ui_series_SeriesViewModel2;

      @KeepFieldType
      CategoryVisibilityViewModel com_nativeplayer_ui_viewmodel_CategoryVisibilityViewModel2;

      @KeepFieldType
      SplashViewModel com_nativeplayer_ui_splash_SplashViewModel2;

      @KeepFieldType
      PlayerSettingsViewModel com_nativeplayer_ui_settings_PlayerSettingsViewModel2;

      @KeepFieldType
      PlayerViewModel com_nativeplayer_ui_player_PlayerViewModel2;

      @KeepFieldType
      LoginViewModel com_nativeplayer_ui_login_LoginViewModel2;

      @KeepFieldType
      SeriesDetailViewModel com_nativeplayer_ui_series_SeriesDetailViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.nativeplayer.ui.viewmodel.CategoryVisibilityViewModel 
          return (T) new CategoryVisibilityViewModel(singletonCImpl.providePreferencesManagerProvider.get(), singletonCImpl.provideIptvRepositoryProvider.get());

          case 1: // com.nativeplayer.ui.favorites.FavoritesViewModel 
          return (T) new FavoritesViewModel(singletonCImpl.provideFavoriteRepositoryProvider.get());

          case 2: // com.nativeplayer.ui.home.HomeViewModel 
          return (T) new HomeViewModel(singletonCImpl.provideWatchHistoryRepositoryProvider.get(), singletonCImpl.provideIptvRepositoryProvider.get(), ApplicationContextModule_ProvideApplicationFactory.provideApplication(singletonCImpl.applicationContextModule));

          case 3: // com.nativeplayer.ui.splash.IntroViewModel 
          return (T) new IntroViewModel(singletonCImpl.provideIptvRepositoryProvider.get(), singletonCImpl.providePreferencesManagerProvider.get());

          case 4: // com.nativeplayer.ui.live.LiveViewModel 
          return (T) new LiveViewModel(singletonCImpl.provideIptvRepositoryProvider.get(), singletonCImpl.provideFavoriteRepositoryProvider.get(), singletonCImpl.providePreferencesManagerProvider.get());

          case 5: // com.nativeplayer.ui.login.LoginViewModel 
          return (T) new LoginViewModel(singletonCImpl.provideIptvRepositoryProvider.get(), singletonCImpl.providePreferencesManagerProvider.get());

          case 6: // com.nativeplayer.ui.movies.MovieDetailViewModel 
          return (T) new MovieDetailViewModel(singletonCImpl.provideFavoriteRepositoryProvider.get(), singletonCImpl.provideWatchHistoryRepositoryProvider.get(), singletonCImpl.provideIptvRepositoryProvider.get());

          case 7: // com.nativeplayer.ui.movies.MoviesViewModel 
          return (T) new MoviesViewModel(singletonCImpl.provideIptvRepositoryProvider.get(), singletonCImpl.provideFavoriteRepositoryProvider.get(), singletonCImpl.providePreferencesManagerProvider.get());

          case 8: // com.nativeplayer.ui.settings.PlayerSettingsViewModel 
          return (T) new PlayerSettingsViewModel(singletonCImpl.providePreferencesManagerProvider.get());

          case 9: // com.nativeplayer.ui.player.PlayerViewModel 
          return (T) new PlayerViewModel(singletonCImpl.provideWatchHistoryRepositoryProvider.get(), singletonCImpl.providePreferencesManagerProvider.get(), singletonCImpl.provideIptvRepositoryProvider.get());

          case 10: // com.nativeplayer.ui.resume.ResumeViewModel 
          return (T) new ResumeViewModel(singletonCImpl.provideWatchHistoryRepositoryProvider.get());

          case 11: // com.nativeplayer.ui.search.SearchViewModel 
          return (T) new SearchViewModel(singletonCImpl.provideIptvRepositoryProvider.get());

          case 12: // com.nativeplayer.ui.series.SeriesDetailViewModel 
          return (T) new SeriesDetailViewModel(singletonCImpl.provideIptvRepositoryProvider.get(), singletonCImpl.provideFavoriteRepositoryProvider.get(), singletonCImpl.provideWatchHistoryRepositoryProvider.get());

          case 13: // com.nativeplayer.ui.series.SeriesViewModel 
          return (T) new SeriesViewModel(singletonCImpl.provideIptvRepositoryProvider.get(), singletonCImpl.provideFavoriteRepositoryProvider.get(), singletonCImpl.providePreferencesManagerProvider.get());

          case 14: // com.nativeplayer.ui.splash.SplashViewModel 
          return (T) new SplashViewModel(singletonCImpl.provideIptvRepositoryProvider.get(), singletonCImpl.providePreferencesManagerProvider.get(), singletonCImpl.provideWatchHistoryRepositoryProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends NativePlayerApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends NativePlayerApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends NativePlayerApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<SharedPreferences> provideSharedPreferencesProvider;

    private Provider<PreferencesManager> providePreferencesManagerProvider;

    private Provider<OkHttpClient> provideOkHttpClientProvider;

    private Provider<IptvRepository> provideIptvRepositoryProvider;

    private Provider<AppDatabase> provideAppDatabaseProvider;

    private Provider<FavoriteDao> provideFavoriteDaoProvider;

    private Provider<FavoriteRepository> provideFavoriteRepositoryProvider;

    private Provider<WatchHistoryDao> provideWatchHistoryDaoProvider;

    private Provider<WatchHistoryRepository> provideWatchHistoryRepositoryProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideSharedPreferencesProvider = DoubleCheck.provider(new SwitchingProvider<SharedPreferences>(singletonCImpl, 1));
      this.providePreferencesManagerProvider = DoubleCheck.provider(new SwitchingProvider<PreferencesManager>(singletonCImpl, 0));
      this.provideOkHttpClientProvider = DoubleCheck.provider(new SwitchingProvider<OkHttpClient>(singletonCImpl, 3));
      this.provideIptvRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<IptvRepository>(singletonCImpl, 2));
      this.provideAppDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<AppDatabase>(singletonCImpl, 6));
      this.provideFavoriteDaoProvider = DoubleCheck.provider(new SwitchingProvider<FavoriteDao>(singletonCImpl, 5));
      this.provideFavoriteRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<FavoriteRepository>(singletonCImpl, 4));
      this.provideWatchHistoryDaoProvider = DoubleCheck.provider(new SwitchingProvider<WatchHistoryDao>(singletonCImpl, 8));
      this.provideWatchHistoryRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<WatchHistoryRepository>(singletonCImpl, 7));
    }

    @Override
    public void injectNativePlayerApp(NativePlayerApp nativePlayerApp) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return ImmutableSet.<Boolean>of();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.nativeplayer.data.repository.PreferencesManager 
          return (T) AppModule_ProvidePreferencesManagerFactory.providePreferencesManager(singletonCImpl.provideSharedPreferencesProvider.get());

          case 1: // android.content.SharedPreferences 
          return (T) AppModule_ProvideSharedPreferencesFactory.provideSharedPreferences(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 2: // com.nativeplayer.data.repository.IptvRepository 
          return (T) AppModule_ProvideIptvRepositoryFactory.provideIptvRepository(singletonCImpl.provideOkHttpClientProvider.get(), singletonCImpl.providePreferencesManagerProvider.get());

          case 3: // okhttp3.OkHttpClient 
          return (T) AppModule_ProvideOkHttpClientFactory.provideOkHttpClient();

          case 4: // com.nativeplayer.data.repository.FavoriteRepository 
          return (T) AppModule_ProvideFavoriteRepositoryFactory.provideFavoriteRepository(singletonCImpl.provideFavoriteDaoProvider.get());

          case 5: // com.nativeplayer.data.repository.FavoriteDao 
          return (T) AppModule_ProvideFavoriteDaoFactory.provideFavoriteDao(singletonCImpl.provideAppDatabaseProvider.get());

          case 6: // com.nativeplayer.data.repository.AppDatabase 
          return (T) AppModule_ProvideAppDatabaseFactory.provideAppDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 7: // com.nativeplayer.data.repository.WatchHistoryRepository 
          return (T) AppModule_ProvideWatchHistoryRepositoryFactory.provideWatchHistoryRepository(singletonCImpl.provideWatchHistoryDaoProvider.get());

          case 8: // com.nativeplayer.data.repository.WatchHistoryDao 
          return (T) AppModule_ProvideWatchHistoryDaoFactory.provideWatchHistoryDao(singletonCImpl.provideAppDatabaseProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
