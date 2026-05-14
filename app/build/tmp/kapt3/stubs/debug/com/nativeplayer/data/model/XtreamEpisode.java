package com.nativeplayer.data.model;

import com.google.gson.annotations.SerializedName;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u001f\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0087\b\u0018\u00002\u00020\u0001BY\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\b\u001a\u0004\u0018\u00010\t\u0012\b\u0010\n\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u000b\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\f\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\r\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u000eJ\t\u0010\u001d\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001e\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001f\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010 \u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010!\u001a\u0004\u0018\u00010\tH\u00c6\u0003J\u000b\u0010\"\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010#\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u0010\u0010$\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003\u00a2\u0006\u0002\u0010\u001aJ\t\u0010%\u001a\u00020\u0003H\u00c6\u0003Jr\u0010&\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\r\u001a\u00020\u0003H\u00c6\u0001\u00a2\u0006\u0002\u0010\'J\u0013\u0010(\u001a\u00020)2\b\u0010*\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010+\u001a\u00020\u0005H\u00d6\u0001J\t\u0010,\u001a\u00020\u0003H\u00d6\u0001R\u0018\u0010\u000b\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0018\u0010\u0007\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010R\u0018\u0010\n\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0010R\u0016\u0010\r\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0010R\u0016\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0010R\u0013\u0010\b\u001a\u0004\u0018\u00010\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u001a\u0010\f\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u001b\u001a\u0004\b\u0019\u0010\u001aR\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0010\u00a8\u0006-"}, d2 = {"Lcom/nativeplayer/data/model/XtreamEpisode;", "", "id", "", "episodeNum", "", "title", "containerExtension", "info", "Lcom/nativeplayer/data/model/XtreamEpisodeInfo;", "customSid", "added", "season", "directSource", "(Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Lcom/nativeplayer/data/model/XtreamEpisodeInfo;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;)V", "getAdded", "()Ljava/lang/String;", "getContainerExtension", "getCustomSid", "getDirectSource", "getEpisodeNum", "()I", "getId", "getInfo", "()Lcom/nativeplayer/data/model/XtreamEpisodeInfo;", "getSeason", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getTitle", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Lcom/nativeplayer/data/model/XtreamEpisodeInfo;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;)Lcom/nativeplayer/data/model/XtreamEpisode;", "equals", "", "other", "hashCode", "toString", "app_debug"})
public final class XtreamEpisode {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String id = null;
    @com.google.gson.annotations.SerializedName(value = "episode_num")
    private final int episodeNum = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String title = null;
    @com.google.gson.annotations.SerializedName(value = "container_extension")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String containerExtension = null;
    @org.jetbrains.annotations.Nullable()
    private final com.nativeplayer.data.model.XtreamEpisodeInfo info = null;
    @com.google.gson.annotations.SerializedName(value = "custom_sid")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String customSid = null;
    @com.google.gson.annotations.SerializedName(value = "added")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String added = null;
    @com.google.gson.annotations.SerializedName(value = "season")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer season = null;
    @com.google.gson.annotations.SerializedName(value = "direct_source")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String directSource = null;
    
    public XtreamEpisode(@org.jetbrains.annotations.NotNull()
    java.lang.String id, int episodeNum, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.Nullable()
    java.lang.String containerExtension, @org.jetbrains.annotations.Nullable()
    com.nativeplayer.data.model.XtreamEpisodeInfo info, @org.jetbrains.annotations.Nullable()
    java.lang.String customSid, @org.jetbrains.annotations.Nullable()
    java.lang.String added, @org.jetbrains.annotations.Nullable()
    java.lang.Integer season, @org.jetbrains.annotations.NotNull()
    java.lang.String directSource) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getId() {
        return null;
    }
    
    public final int getEpisodeNum() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getContainerExtension() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.nativeplayer.data.model.XtreamEpisodeInfo getInfo() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getCustomSid() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getAdded() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getSeason() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDirectSource() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    public final int component2() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.nativeplayer.data.model.XtreamEpisodeInfo component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.nativeplayer.data.model.XtreamEpisode copy(@org.jetbrains.annotations.NotNull()
    java.lang.String id, int episodeNum, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.Nullable()
    java.lang.String containerExtension, @org.jetbrains.annotations.Nullable()
    com.nativeplayer.data.model.XtreamEpisodeInfo info, @org.jetbrains.annotations.Nullable()
    java.lang.String customSid, @org.jetbrains.annotations.Nullable()
    java.lang.String added, @org.jetbrains.annotations.Nullable()
    java.lang.Integer season, @org.jetbrains.annotations.NotNull()
    java.lang.String directSource) {
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