.class Lorg/apache/cordova/FileTransfer$TrackingHTTPInputStream;
.super Lorg/apache/cordova/FileTransfer$TrackingInputStream;
.source "FileTransfer.java"


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Lorg/apache/cordova/FileTransfer;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0xa
    name = "TrackingHTTPInputStream"
.end annotation


# instance fields
.field private bytesRead:J


# direct methods
.method public constructor <init>(Ljava/io/InputStream;)V
    .locals 2
    .parameter "stream"

    .prologue
    .line 151
    invoke-direct {p0, p1}, Lorg/apache/cordova/FileTransfer$TrackingInputStream;-><init>(Ljava/io/InputStream;)V

    .line 149
    const-wide/16 v0, 0x0

    iput-wide v0, p0, Lorg/apache/cordova/FileTransfer$TrackingHTTPInputStream;->bytesRead:J

    .line 152
    return-void
.end method

.method private updateBytesRead(I)I
    .locals 4
    .parameter "newBytesRead"

    .prologue
    .line 155
    const/4 v0, -0x1

    if-eq p1, v0, :cond_0

    .line 156
    iget-wide v0, p0, Lorg/apache/cordova/FileTransfer$TrackingHTTPInputStream;->bytesRead:J

    int-to-long v2, p1

    add-long/2addr v0, v2

    iput-wide v0, p0, Lorg/apache/cordova/FileTransfer$TrackingHTTPInputStream;->bytesRead:J

    .line 158
    :cond_0
    return p1
.end method


# virtual methods
.method public getTotalRawBytesRead()J
    .locals 2

    .prologue
    .line 177
    iget-wide v0, p0, Lorg/apache/cordova/FileTransfer$TrackingHTTPInputStream;->bytesRead:J

    return-wide v0
.end method

.method public read()I
    .locals 1
    .annotation system Ldalvik/annotation/Throws;
        value = {
            Ljava/io/IOException;
        }
    .end annotation

    .prologue
    .line 163
    invoke-super {p0}, Lorg/apache/cordova/FileTransfer$TrackingInputStream;->read()I

    move-result v0

    invoke-direct {p0, v0}, Lorg/apache/cordova/FileTransfer$TrackingHTTPInputStream;->updateBytesRead(I)I

    move-result v0

    return v0
.end method

.method public read([B)I
    .locals 1
    .parameter "buffer"
    .annotation system Ldalvik/annotation/Throws;
        value = {
            Ljava/io/IOException;
        }
    .end annotation

    .prologue
    .line 168
    invoke-super {p0, p1}, Lorg/apache/cordova/FileTransfer$TrackingInputStream;->read([B)I

    move-result v0

    invoke-direct {p0, v0}, Lorg/apache/cordova/FileTransfer$TrackingHTTPInputStream;->updateBytesRead(I)I

    move-result v0

    return v0
.end method

.method public read([BII)I
    .locals 1
    .parameter "bytes"
    .parameter "offset"
    .parameter "count"
    .annotation system Ldalvik/annotation/Throws;
        value = {
            Ljava/io/IOException;
        }
    .end annotation

    .prologue
    .line 173
    invoke-super {p0, p1, p2, p3}, Lorg/apache/cordova/FileTransfer$TrackingInputStream;->read([BII)I

    move-result v0

    invoke-direct {p0, v0}, Lorg/apache/cordova/FileTransfer$TrackingHTTPInputStream;->updateBytesRead(I)I

    move-result v0

    return v0
.end method
