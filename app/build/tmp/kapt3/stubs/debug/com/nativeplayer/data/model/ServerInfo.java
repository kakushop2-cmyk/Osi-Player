package com.nativeplayer.data.model;

import com.google.gson.annotations.SerializedName;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0016\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001BE\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\fJ\t\u0010\u0017\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001c\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001d\u001a\u00020\nH\u00c6\u0003J\t\u0010\u001e\u001a\u00020\u0003H\u00c6\u0003JY\u0010\u001f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010 \u001a\u00020!2\b\u0010\"\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010#\u001a\u00020$H\u00d6\u0001J\t\u0010%\u001a\u00020\u0003H\u00d6\u0001R\u0016\u0010\u0005\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000eR\u0016\u0010\u0007\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000eR\u0016\u0010\u0006\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000eR\u0016\u0010\u000b\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u000eR\u0016\u0010\t\u001a\u00020\n8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u000eR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u000e\u00a8\u0006&"}, d2 = {"Lcom/nativeplayer/data/model/ServerInfo;", "", "url", "", "port", "httpsPort", "serverProtocol", "rtmpPort", "timezone", "timestampNow", "", "timeNow", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;)V", "getHttpsPort", "()Ljava/lang/String;", "getPort", "getRtmpPort", "getServerProtocol", "getTimeNow", "getTimestampNow", "()J", "getTimezone", "getUrl", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
public final class ServerInfo {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String url = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String port = null;
    @com.google.gson.annotations.SerializedName(value = "https_port")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String httpsPort = null;
    @com.google.gson.annotations.SerializedName(value = "server_protocol")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String serverProtocol = null;
    @com.google.gson.annotations.SerializedName(value = "rtmp_port")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String rtmpPort = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String timezone = null;
    @com.google.gson.annotations.SerializedName(value = "timestamp_now")
    private final long timestampNow = 0L;
    @com.google.gson.annotations.SerializedName(value = "time_now")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String timeNow = null;
    
    public ServerInfo(@org.jetbrains.annotations.NotNull()
    java.lang.String url, @org.jetbrains.annotations.NotNull()
    java.lang.String port, @org.jetbrains.annotations.NotNull()
    java.lang.String httpsPort, @org.jetbrains.annotations.NotNull()
    java.lang.String serverProtocol, @org.jetbrains.annotations.NotNull()
    java.lang.String rtmpPort, @org.jetbrains.annotations.NotNull()
    java.lang.String timezone, long timestampNow, @org.jetbrains.annotations.NotNull()
    java.lang.String timeNow) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getUrl() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPort() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getHttpsPort() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getServerProtocol() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getRtmpPort() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTimezone() {
        return null;
    }
    
    public final long getTimestampNow() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTimeNow() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component6() {
        return null;
    }
    
    public final long component7() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.nativeplayer.data.model.ServerInfo copy(@org.jetbrains.annotations.NotNull()
    java.lang.String url, @org.jetbrains.annotations.NotNull()
    java.lang.String port, @org.jetbrains.annotations.NotNull()
    java.lang.String httpsPort, @org.jetbrains.annotations.NotNull()
    java.lang.String serverProtocol, @org.jetbrains.annotations.NotNull()
    java.lang.String rtmpPort, @org.jetbrains.annotations.NotNull()
    java.lang.String timezone, long timestampNow, @org.jetbrains.annotations.NotNull()
    java.lang.String timeNow) {
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