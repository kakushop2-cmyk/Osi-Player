package com.nativeplayer.ui.live;

/**
 * Compose State für die Synchronisation des ausgewählten Kanals
 * zwischen PlayerScreen (Fullscreen) und LiveScreen.
 *
 * [selectedChannelId] ist ein [mutableStateOf], damit Änderungen
 * (gesetzt in PlayerScreen.cleanupAndGoBack) sofort eine Recomposition
 * von LiveScreen auslösen, die dann per LaunchedEffect den
 * korrekten Kanal auswählt.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\b\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R/\u0010\u0005\u001a\u0004\u0018\u00010\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u00048F@FX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b\n\u0010\u000b\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\t\u00a8\u0006\f"}, d2 = {"Lcom/nativeplayer/ui/live/BackNavigationState;", "", "()V", "<set-?>", "", "selectedChannelId", "getSelectedChannelId", "()Ljava/lang/String;", "setSelectedChannelId", "(Ljava/lang/String;)V", "selectedChannelId$delegate", "Landroidx/compose/runtime/MutableState;", "app_debug"})
public final class BackNavigationState {
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.runtime.MutableState selectedChannelId$delegate = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.nativeplayer.ui.live.BackNavigationState INSTANCE = null;
    
    private BackNavigationState() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSelectedChannelId() {
        return null;
    }
    
    public final void setSelectedChannelId(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
}