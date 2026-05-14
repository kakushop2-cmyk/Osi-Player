package com.nativeplayer.data.model;

import com.google.gson.annotations.SerializedName;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B;\u0012\u000e\u0010\u0002\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\u001a\u0010\u0007\u001a\u0016\u0012\u0004\u0012\u00020\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u0003\u0018\u00010\b\u00a2\u0006\u0002\u0010\u000bJ\u0011\u0010\u0012\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\u0013\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J\u001d\u0010\u0014\u001a\u0016\u0012\u0004\u0012\u00020\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u0003\u0018\u00010\bH\u00c6\u0003JE\u0010\u0015\u001a\u00020\u00002\u0010\b\u0002\u0010\u0002\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u001c\b\u0002\u0010\u0007\u001a\u0016\u0012\u0004\u0012\u00020\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u0003\u0018\u00010\bH\u00c6\u0001J\u0013\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0019\u001a\u00020\u001aH\u00d6\u0001J\t\u0010\u001b\u001a\u00020\tH\u00d6\u0001R%\u0010\u0007\u001a\u0016\u0012\u0004\u0012\u00020\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u0003\u0018\u00010\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0019\u0010\u0002\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011\u00a8\u0006\u001c"}, d2 = {"Lcom/nativeplayer/data/model/XtreamSeriesInfo;", "", "seasons", "", "Lcom/nativeplayer/data/model/XtreamSeason;", "info", "Lcom/nativeplayer/data/model/XtreamSeriesInfoDetails;", "episodes", "", "", "Lcom/nativeplayer/data/model/XtreamEpisode;", "(Ljava/util/List;Lcom/nativeplayer/data/model/XtreamSeriesInfoDetails;Ljava/util/Map;)V", "getEpisodes", "()Ljava/util/Map;", "getInfo", "()Lcom/nativeplayer/data/model/XtreamSeriesInfoDetails;", "getSeasons", "()Ljava/util/List;", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "app_release"})
public final class XtreamSeriesInfo {
    @org.jetbrains.annotations.Nullable()
    private final java.util.List<com.nativeplayer.data.model.XtreamSeason> seasons = null;
    @org.jetbrains.annotations.Nullable()
    private final com.nativeplayer.data.model.XtreamSeriesInfoDetails info = null;
    @org.jetbrains.annotations.Nullable()
    private final java.util.Map<java.lang.String, java.util.List<com.nativeplayer.data.model.XtreamEpisode>> episodes = null;
    
    public XtreamSeriesInfo(@org.jetbrains.annotations.Nullable()
    java.util.List<com.nativeplayer.data.model.XtreamSeason> seasons, @org.jetbrains.annotations.Nullable()
    com.nativeplayer.data.model.XtreamSeriesInfoDetails info, @org.jetbrains.annotations.Nullable()
    java.util.Map<java.lang.String, ? extends java.util.List<com.nativeplayer.data.model.XtreamEpisode>> episodes) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.nativeplayer.data.model.XtreamSeason> getSeasons() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.nativeplayer.data.model.XtreamSeriesInfoDetails getInfo() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.Map<java.lang.String, java.util.List<com.nativeplayer.data.model.XtreamEpisode>> getEpisodes() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.nativeplayer.data.model.XtreamSeason> component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.nativeplayer.data.model.XtreamSeriesInfoDetails component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.Map<java.lang.String, java.util.List<com.nativeplayer.data.model.XtreamEpisode>> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.nativeplayer.data.model.XtreamSeriesInfo copy(@org.jetbrains.annotations.Nullable()
    java.util.List<com.nativeplayer.data.model.XtreamSeason> seasons, @org.jetbrains.annotations.Nullable()
    com.nativeplayer.data.model.XtreamSeriesInfoDetails info, @org.jetbrains.annotations.Nullable()
    java.util.Map<java.lang.String, ? extends java.util.List<com.nativeplayer.data.model.XtreamEpisode>> episodes) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}