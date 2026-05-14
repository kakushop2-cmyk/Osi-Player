# Add project specific ProGuard rules here.

# Keep Media3
-keep class androidx.media3.** { *; }
-dontwarn androidx.media3.**

# Keep IJKPlayer
-keep class tv.danmaku.ijk.** { *; }
-dontwarn tv.danmaku.ijk.**

# Keep Dagger/Hilt
-dontwarn dagger.**
-keep class dagger.** { *; }

# Keep OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**

# Keep Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.nativeplayer.data.model.** { *; }
