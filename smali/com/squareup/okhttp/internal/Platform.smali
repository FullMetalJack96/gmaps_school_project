.class public Lcom/squareup/okhttp/internal/Platform;
.super Ljava/lang/Object;
.source "Platform.java"


# annotations
.annotation system Ldalvik/annotation/MemberClasses;
    value = {
        Lcom/squareup/okhttp/internal/Platform$1;,
        Lcom/squareup/okhttp/internal/Platform$JettyNpnProvider;,
        Lcom/squareup/okhttp/internal/Platform$JdkWithJettyNpnPlatform;,
        Lcom/squareup/okhttp/internal/Platform$Android41;,
        Lcom/squareup/okhttp/internal/Platform$Android23;,
        Lcom/squareup/okhttp/internal/Platform$Java5;
    }
.end annotation


# static fields
.field private static final PLATFORM:Lcom/squareup/okhttp/internal/Platform;


# instance fields
.field private deflaterConstructor:Ljava/lang/reflect/Constructor;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Ljava/lang/reflect/Constructor",
            "<",
            "Ljava/util/zip/DeflaterOutputStream;",
            ">;"
        }
    .end annotation
.end field


# direct methods
.method static constructor <clinit>()V
    .locals 1

    .prologue
    .line 52
    invoke-static {}, Lcom/squareup/okhttp/internal/Platform;->findPlatform()Lcom/squareup/okhttp/internal/Platform;

    move-result-object v0

    sput-object v0, Lcom/squareup/okhttp/internal/Platform;->PLATFORM:Lcom/squareup/okhttp/internal/Platform;

    return-void
.end method

.method public constructor <init>()V
    .locals 0

    .prologue
    .line 51
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    .line 351
    return-void
.end method

.method private static findPlatform()Lcom/squareup/okhttp/internal/Platform;
    .locals 20

    .prologue
    .line 144
    :try_start_0
    const-class v1, Ljava/net/NetworkInterface;

    const-string v8, "getMTU"

    const/4 v9, 0x0

    new-array v9, v9, [Ljava/lang/Class;

    invoke-virtual {v1, v8, v9}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    :try_end_0
    .catch Ljava/lang/NoSuchMethodException; {:try_start_0 .. :try_end_0} :catch_0

    move-result-object v2

    .line 154
    .local v2, getMtu:Ljava/lang/reflect/Method;
    :try_start_1
    const-string v1, "org.apache.harmony.xnet.provider.jsse.OpenSSLSocketImpl"

    invoke-static {v1}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v3

    .line 155
    .local v3, openSslSocketClass:Ljava/lang/Class;,"Ljava/lang/Class<*>;"
    const-string v1, "setUseSessionTickets"

    const/4 v8, 0x1

    new-array v8, v8, [Ljava/lang/Class;

    const/4 v9, 0x0

    sget-object v19, Ljava/lang/Boolean;->TYPE:Ljava/lang/Class;

    aput-object v19, v8, v9

    invoke-virtual {v3, v1, v8}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v4

    .line 156
    .local v4, setUseSessionTickets:Ljava/lang/reflect/Method;
    const-string v1, "setHostname"

    const/4 v8, 0x1

    new-array v8, v8, [Ljava/lang/Class;

    const/4 v9, 0x0

    const-class v19, Ljava/lang/String;

    aput-object v19, v8, v9

    invoke-virtual {v3, v1, v8}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    :try_end_1
    .catch Ljava/lang/ClassNotFoundException; {:try_start_1 .. :try_end_1} :catch_5
    .catch Ljava/lang/NoSuchMethodException; {:try_start_1 .. :try_end_1} :catch_2

    move-result-object v5

    .line 160
    .local v5, setHostname:Ljava/lang/reflect/Method;
    :try_start_2
    const-string v1, "setNpnProtocols"

    const/4 v8, 0x1

    new-array v8, v8, [Ljava/lang/Class;

    const/4 v9, 0x0

    const-class v19, [B

    aput-object v19, v8, v9

    invoke-virtual {v3, v1, v8}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v6

    .line 161
    .local v6, setNpnProtocols:Ljava/lang/reflect/Method;
    const-string v1, "getNpnSelectedProtocol"

    const/4 v8, 0x0

    new-array v8, v8, [Ljava/lang/Class;

    invoke-virtual {v3, v1, v8}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v7

    .line 162
    .local v7, getNpnSelectedProtocol:Ljava/lang/reflect/Method;
    new-instance v1, Lcom/squareup/okhttp/internal/Platform$Android41;

    const/4 v8, 0x0

    invoke-direct/range {v1 .. v8}, Lcom/squareup/okhttp/internal/Platform$Android41;-><init>(Ljava/lang/reflect/Method;Ljava/lang/Class;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;Lcom/squareup/okhttp/internal/Platform$1;)V
    :try_end_2
    .catch Ljava/lang/NoSuchMethodException; {:try_start_2 .. :try_end_2} :catch_1
    .catch Ljava/lang/ClassNotFoundException; {:try_start_2 .. :try_end_2} :catch_5

    .line 190
    .end local v2           #getMtu:Ljava/lang/reflect/Method;
    .end local v3           #openSslSocketClass:Ljava/lang/Class;,"Ljava/lang/Class<*>;"
    .end local v4           #setUseSessionTickets:Ljava/lang/reflect/Method;
    .end local v5           #setHostname:Ljava/lang/reflect/Method;
    .end local v6           #setNpnProtocols:Ljava/lang/reflect/Method;
    .end local v7           #getNpnSelectedProtocol:Ljava/lang/reflect/Method;
    :goto_0
    return-object v1

    .line 145
    :catch_0
    move-exception v14

    .line 146
    .local v14, e:Ljava/lang/NoSuchMethodException;
    new-instance v1, Lcom/squareup/okhttp/internal/Platform;

    invoke-direct {v1}, Lcom/squareup/okhttp/internal/Platform;-><init>()V

    goto :goto_0

    .line 164
    .end local v14           #e:Ljava/lang/NoSuchMethodException;
    .restart local v2       #getMtu:Ljava/lang/reflect/Method;
    .restart local v3       #openSslSocketClass:Ljava/lang/Class;,"Ljava/lang/Class<*>;"
    .restart local v4       #setUseSessionTickets:Ljava/lang/reflect/Method;
    .restart local v5       #setHostname:Ljava/lang/reflect/Method;
    :catch_1
    move-exception v15

    .line 165
    .local v15, ignored:Ljava/lang/NoSuchMethodException;
    :try_start_3
    new-instance v8, Lcom/squareup/okhttp/internal/Platform$Android23;

    const/4 v13, 0x0

    move-object v9, v2

    move-object v10, v3

    move-object v11, v4

    move-object v12, v5

    invoke-direct/range {v8 .. v13}, Lcom/squareup/okhttp/internal/Platform$Android23;-><init>(Ljava/lang/reflect/Method;Ljava/lang/Class;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;Lcom/squareup/okhttp/internal/Platform$1;)V
    :try_end_3
    .catch Ljava/lang/ClassNotFoundException; {:try_start_3 .. :try_end_3} :catch_5
    .catch Ljava/lang/NoSuchMethodException; {:try_start_3 .. :try_end_3} :catch_2

    move-object v1, v8

    goto :goto_0

    .line 169
    .end local v3           #openSslSocketClass:Ljava/lang/Class;,"Ljava/lang/Class<*>;"
    .end local v4           #setUseSessionTickets:Ljava/lang/reflect/Method;
    .end local v5           #setHostname:Ljava/lang/reflect/Method;
    .end local v15           #ignored:Ljava/lang/NoSuchMethodException;
    :catch_2
    move-exception v1

    .line 175
    :goto_1
    :try_start_4
    const-string v17, "org.eclipse.jetty.npn.NextProtoNego"

    .line 176
    .local v17, npnClassName:Ljava/lang/String;
    invoke-static/range {v17 .. v17}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v16

    .line 177
    .local v16, nextProtoNegoClass:Ljava/lang/Class;,"Ljava/lang/Class<*>;"
    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    move-object/from16 v0, v17

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    const-string v8, "$Provider"

    invoke-virtual {v1, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-static {v1}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v18

    .line 178
    .local v18, providerClass:Ljava/lang/Class;,"Ljava/lang/Class<*>;"
    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    move-object/from16 v0, v17

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    const-string v8, "$ClientProvider"

    invoke-virtual {v1, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-static {v1}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v12

    .line 179
    .local v12, clientProviderClass:Ljava/lang/Class;,"Ljava/lang/Class<*>;"
    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    move-object/from16 v0, v17

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    const-string v8, "$ServerProvider"

    invoke-virtual {v1, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-static {v1}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v13

    .line 180
    .local v13, serverProviderClass:Ljava/lang/Class;,"Ljava/lang/Class<*>;"
    const-string v1, "put"

    const/4 v8, 0x2

    new-array v8, v8, [Ljava/lang/Class;

    const/4 v9, 0x0

    const-class v19, Ljavax/net/ssl/SSLSocket;

    aput-object v19, v8, v9

    const/4 v9, 0x1

    aput-object v18, v8, v9

    move-object/from16 v0, v16

    invoke-virtual {v0, v1, v8}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v10

    .line 181
    .local v10, putMethod:Ljava/lang/reflect/Method;
    const-string v1, "get"

    const/4 v8, 0x1

    new-array v8, v8, [Ljava/lang/Class;

    const/4 v9, 0x0

    const-class v19, Ljavax/net/ssl/SSLSocket;

    aput-object v19, v8, v9

    move-object/from16 v0, v16

    invoke-virtual {v0, v1, v8}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v11

    .line 182
    .local v11, getMethod:Ljava/lang/reflect/Method;
    new-instance v8, Lcom/squareup/okhttp/internal/Platform$JdkWithJettyNpnPlatform;

    move-object v9, v2

    invoke-direct/range {v8 .. v13}, Lcom/squareup/okhttp/internal/Platform$JdkWithJettyNpnPlatform;-><init>(Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;Ljava/lang/Class;Ljava/lang/Class;)V
    :try_end_4
    .catch Ljava/lang/ClassNotFoundException; {:try_start_4 .. :try_end_4} :catch_4
    .catch Ljava/lang/NoSuchMethodException; {:try_start_4 .. :try_end_4} :catch_3

    move-object v1, v8

    goto/16 :goto_0

    .line 186
    .end local v10           #putMethod:Ljava/lang/reflect/Method;
    .end local v11           #getMethod:Ljava/lang/reflect/Method;
    .end local v12           #clientProviderClass:Ljava/lang/Class;,"Ljava/lang/Class<*>;"
    .end local v13           #serverProviderClass:Ljava/lang/Class;,"Ljava/lang/Class<*>;"
    .end local v16           #nextProtoNegoClass:Ljava/lang/Class;,"Ljava/lang/Class<*>;"
    .end local v17           #npnClassName:Ljava/lang/String;
    .end local v18           #providerClass:Ljava/lang/Class;,"Ljava/lang/Class<*>;"
    :catch_3
    move-exception v1

    .line 190
    :goto_2
    if-eqz v2, :cond_0

    new-instance v1, Lcom/squareup/okhttp/internal/Platform$Java5;

    const/4 v8, 0x0

    invoke-direct {v1, v2, v8}, Lcom/squareup/okhttp/internal/Platform$Java5;-><init>(Ljava/lang/reflect/Method;Lcom/squareup/okhttp/internal/Platform$1;)V

    goto/16 :goto_0

    :cond_0
    new-instance v1, Lcom/squareup/okhttp/internal/Platform;

    invoke-direct {v1}, Lcom/squareup/okhttp/internal/Platform;-><init>()V

    goto/16 :goto_0

    .line 184
    :catch_4
    move-exception v1

    goto :goto_2

    .line 167
    :catch_5
    move-exception v1

    goto/16 :goto_1
.end method

.method public static get()Lcom/squareup/okhttp/internal/Platform;
    .locals 1

    .prologue
    .line 57
    sget-object v0, Lcom/squareup/okhttp/internal/Platform;->PLATFORM:Lcom/squareup/okhttp/internal/Platform;

    return-object v0
.end method


# virtual methods
.method public enableTlsExtensions(Ljavax/net/ssl/SSLSocket;Ljava/lang/String;)V
    .locals 0
    .parameter "socket"
    .parameter "uriHost"

    .prologue
    .line 80
    return-void
.end method

.method public getMtu(Ljava/net/Socket;)I
    .locals 1
    .parameter "socket"
    .annotation system Ldalvik/annotation/Throws;
        value = {
            Ljava/io/IOException;
        }
    .end annotation

    .prologue
    .line 137
    const/16 v0, 0x578

    return v0
.end method

.method public getNpnSelectedProtocol(Ljavax/net/ssl/SSLSocket;)[B
    .locals 1
    .parameter "socket"

    .prologue
    .line 92
    const/4 v0, 0x0

    return-object v0
.end method

.method public logW(Ljava/lang/String;)V
    .locals 1
    .parameter "warning"

    .prologue
    .line 61
    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    invoke-virtual {v0, p1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    .line 62
    return-void
.end method

.method public newDeflaterOutputStream(Ljava/io/OutputStream;Ljava/util/zip/Deflater;Z)Ljava/io/OutputStream;
    .locals 6
    .parameter "out"
    .parameter "deflater"
    .parameter "syncFlush"

    .prologue
    .line 110
    :try_start_0
    iget-object v0, p0, Lcom/squareup/okhttp/internal/Platform;->deflaterConstructor:Ljava/lang/reflect/Constructor;

    .line 111
    .local v0, constructor:Ljava/lang/reflect/Constructor;,"Ljava/lang/reflect/Constructor<Ljava/util/zip/DeflaterOutputStream;>;"
    if-nez v0, :cond_0

    .line 112
    const-class v2, Ljava/util/zip/DeflaterOutputStream;

    const/4 v3, 0x3

    new-array v3, v3, [Ljava/lang/Class;

    const/4 v4, 0x0

    const-class v5, Ljava/io/OutputStream;

    aput-object v5, v3, v4

    const/4 v4, 0x1

    const-class v5, Ljava/util/zip/Deflater;

    aput-object v5, v3, v4

    const/4 v4, 0x2

    sget-object v5, Ljava/lang/Boolean;->TYPE:Ljava/lang/Class;

    aput-object v5, v3, v4

    invoke-virtual {v2, v3}, Ljava/lang/Class;->getConstructor([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;

    move-result-object v0

    .end local v0           #constructor:Ljava/lang/reflect/Constructor;,"Ljava/lang/reflect/Constructor<Ljava/util/zip/DeflaterOutputStream;>;"
    iput-object v0, p0, Lcom/squareup/okhttp/internal/Platform;->deflaterConstructor:Ljava/lang/reflect/Constructor;

    .line 115
    .restart local v0       #constructor:Ljava/lang/reflect/Constructor;,"Ljava/lang/reflect/Constructor<Ljava/util/zip/DeflaterOutputStream;>;"
    :cond_0
    const/4 v2, 0x3

    new-array v2, v2, [Ljava/lang/Object;

    const/4 v3, 0x0

    aput-object p1, v2, v3

    const/4 v3, 0x1

    aput-object p2, v2, v3

    const/4 v3, 0x2

    invoke-static {p3}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;

    move-result-object v4

    aput-object v4, v2, v3

    invoke-virtual {v0, v2}, Ljava/lang/reflect/Constructor;->newInstance([Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Ljava/io/OutputStream;
    :try_end_0
    .catch Ljava/lang/NoSuchMethodException; {:try_start_0 .. :try_end_0} :catch_0
    .catch Ljava/lang/reflect/InvocationTargetException; {:try_start_0 .. :try_end_0} :catch_1
    .catch Ljava/lang/InstantiationException; {:try_start_0 .. :try_end_0} :catch_2
    .catch Ljava/lang/IllegalAccessException; {:try_start_0 .. :try_end_0} :catch_3

    return-object v2

    .line 116
    .end local v0           #constructor:Ljava/lang/reflect/Constructor;,"Ljava/lang/reflect/Constructor<Ljava/util/zip/DeflaterOutputStream;>;"
    :catch_0
    move-exception v1

    .line 117
    .local v1, e:Ljava/lang/NoSuchMethodException;
    new-instance v2, Ljava/lang/UnsupportedOperationException;

    const-string v3, "Cannot SPDY; no SYNC_FLUSH available"

    invoke-direct {v2, v3}, Ljava/lang/UnsupportedOperationException;-><init>(Ljava/lang/String;)V

    throw v2

    .line 118
    .end local v1           #e:Ljava/lang/NoSuchMethodException;
    :catch_1
    move-exception v1

    .line 119
    .local v1, e:Ljava/lang/reflect/InvocationTargetException;
    invoke-virtual {v1}, Ljava/lang/reflect/InvocationTargetException;->getCause()Ljava/lang/Throwable;

    move-result-object v2

    instance-of v2, v2, Ljava/lang/RuntimeException;

    if-eqz v2, :cond_1

    invoke-virtual {v1}, Ljava/lang/reflect/InvocationTargetException;->getCause()Ljava/lang/Throwable;

    move-result-object v2

    check-cast v2, Ljava/lang/RuntimeException;

    :goto_0
    throw v2

    :cond_1
    new-instance v2, Ljava/lang/RuntimeException;

    invoke-virtual {v1}, Ljava/lang/reflect/InvocationTargetException;->getCause()Ljava/lang/Throwable;

    move-result-object v3

    invoke-direct {v2, v3}, Ljava/lang/RuntimeException;-><init>(Ljava/lang/Throwable;)V

    goto :goto_0

    .line 121
    .end local v1           #e:Ljava/lang/reflect/InvocationTargetException;
    :catch_2
    move-exception v1

    .line 122
    .local v1, e:Ljava/lang/InstantiationException;
    new-instance v2, Ljava/lang/RuntimeException;

    invoke-direct {v2, v1}, Ljava/lang/RuntimeException;-><init>(Ljava/lang/Throwable;)V

    throw v2

    .line 123
    .end local v1           #e:Ljava/lang/InstantiationException;
    :catch_3
    move-exception v1

    .line 124
    .local v1, e:Ljava/lang/IllegalAccessException;
    new-instance v2, Ljava/lang/AssertionError;

    invoke-direct {v2}, Ljava/lang/AssertionError;-><init>()V

    throw v2
.end method

.method public setNpnProtocols(Ljavax/net/ssl/SSLSocket;[B)V
    .locals 0
    .parameter "socket"
    .parameter "npnProtocols"

    .prologue
    .line 100
    return-void
.end method

.method public supportTlsIntolerantServer(Ljavax/net/ssl/SSLSocket;)V
    .locals 3
    .parameter "socket"

    .prologue
    .line 87
    const/4 v0, 0x1

    new-array v0, v0, [Ljava/lang/String;

    const/4 v1, 0x0

    const-string v2, "SSLv3"

    aput-object v2, v0, v1

    invoke-virtual {p1, v0}, Ljavax/net/ssl/SSLSocket;->setEnabledProtocols([Ljava/lang/String;)V

    .line 88
    return-void
.end method

.method public tagSocket(Ljava/net/Socket;)V
    .locals 0
    .parameter "socket"
    .annotation system Ldalvik/annotation/Throws;
        value = {
            Ljava/net/SocketException;
        }
    .end annotation

    .prologue
    .line 65
    return-void
.end method

.method public toUriLenient(Ljava/net/URL;)Ljava/net/URI;
    .locals 1
    .parameter "url"
    .annotation system Ldalvik/annotation/Throws;
        value = {
            Ljava/net/URISyntaxException;
        }
    .end annotation

    .prologue
    .line 71
    invoke-virtual {p1}, Ljava/net/URL;->toURI()Ljava/net/URI;

    move-result-object v0

    return-object v0
.end method

.method public untagSocket(Ljava/net/Socket;)V
    .locals 0
    .parameter "socket"
    .annotation system Ldalvik/annotation/Throws;
        value = {
            Ljava/net/SocketException;
        }
    .end annotation

    .prologue
    .line 68
    return-void
.end method
