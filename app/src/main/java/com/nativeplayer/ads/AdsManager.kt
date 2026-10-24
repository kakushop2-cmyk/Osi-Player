package com.nativeplayer.ads

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.nativeplayer.BuildConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Genau eine App-Open-Ad pro Kaltstart, über dem Intro-/Ladebildschirm.
 *
 * Play-Policy: Ein Vollbild-Interstitial *vor* dem Splash ist verboten.
 * App-Open-Ads sind das dafür vorgesehene Format ("overlays loading screen").
 * Ohne geladene Anzeige oder ohne Consent startet die App sofort normal.
 */
object AdsManager {

    /** Nur in diesem Fenster nach Prozessstart wird die Anzeige gezeigt. */
    private const val COLD_START_WINDOW_MS = 20_000L

    private val _isAdShowing = MutableStateFlow(false)
    val isAdShowing: StateFlow<Boolean> = _isAdShowing.asStateFlow()

    private var appOpenAd: AppOpenAd? = null
    private var isLoading = false
    private var shownThisProcess = false
    private val processStartedAt = System.currentTimeMillis()

    /** Öffnet den UMP-Dialog zum Ändern/Widerrufen der Werbe-Einwilligung. */
    fun showPrivacyOptions(activity: Activity) {
        UserMessagingPlatform.showPrivacyOptionsForm(activity) { }
    }

    fun initialize(activity: Activity) {
        val consentInformation = UserMessagingPlatform.getConsentInformation(activity)
        val params = ConsentRequestParameters.Builder()
            .apply {
                if (BuildConfig.DEBUG) {
                    setConsentDebugSettings(
                        ConsentDebugSettings.Builder(activity)
                            .setDebugGeography(
                                ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA
                            )
                            .build()
                    )
                }
            }
            .build()

        consentInformation.requestConsentInfoUpdate(
            activity,
            params,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) {
                    startAdsIfAllowed(activity, consentInformation)
                }
            },
            {
                // Bei fehlgeschlagenem Consent-Update keine Anzeigen anfordern.
                startAdsIfAllowed(activity, consentInformation)
            }
        )
    }

    private fun startAdsIfAllowed(activity: Activity, consentInformation: ConsentInformation) {
        if (!consentInformation.canRequestAds()) return
        MobileAds.initialize(activity) {
            loadAppOpenAd(activity)
        }
    }

    private fun loadAppOpenAd(activity: Activity) {
        if (appOpenAd != null || isLoading || shownThisProcess) return
        isLoading = true
        AppOpenAd.load(
            activity,
            BuildConfig.ADMOB_APP_OPEN_UNIT_ID,
            AdRequest.Builder().build(),
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    isLoading = false
                    appOpenAd = ad
                    maybeShow(activity)
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    isLoading = false
                    appOpenAd = null
                }
            }
        )
    }

    private fun maybeShow(activity: Activity) {
        if (shownThisProcess || activity.isFinishing || activity.isDestroyed) return
        if (System.currentTimeMillis() - processStartedAt > COLD_START_WINDOW_MS) return

        val ad = appOpenAd ?: return
        shownThisProcess = true
        appOpenAd = null
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                _isAdShowing.value = true
            }

            override fun onAdDismissedFullScreenContent() {
                _isAdShowing.value = false
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                _isAdShowing.value = false
            }
        }
        ad.show(activity)
    }
}
