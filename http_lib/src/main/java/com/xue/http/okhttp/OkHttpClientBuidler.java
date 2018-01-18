package com.xue.http.okhttp;

import android.text.TextUtils;

import com.xue.http.HttpConstant;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.OkHttpClient;
import okhttp3.internal.Platform;
import okio.Buffer;

/**
 * Created by xfilshy on 15/11/7.
 */
class OkHttpClientBuidler {

    private static OkHttpClientBuidler instance;

    OkHttpClient.Builder builder;

    private OkHttpClient okHttpClient;

    private OkHttpClientBuidler(String opensslSecret) {
        builder = new OkHttpClient.Builder();
        builder.connectTimeout(HttpConstant.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(HttpConstant.READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(HttpConstant.READ_TIMEOUT, TimeUnit.MILLISECONDS);

        if (!TextUtils.isEmpty(opensslSecret)) {
            SSLContext sslContext = sslContextForTrustedCertificates(trustedCertificatesInputStream(opensslSecret));
            builder.sslSocketFactory(sslContext.getSocketFactory(), Platform.get().trustManager(sslContext.getSocketFactory()));
        }
    }

    public static OkHttpClientBuidler get(String opensslSecret) {
        synchronized (OkHttpClientBuidler.class) {
            if (instance == null) {
                instance = new OkHttpClientBuidler(opensslSecret);
            }

            return instance;
        }
    }

    public OkHttpClient getOkHttpClient() {
        return builder.build();
    }

    private InputStream trustedCertificatesInputStream(String opensslSecret) {
        return new Buffer().writeUtf8(opensslSecret).inputStream();
    }

    public SSLContext sslContextForTrustedCertificates(InputStream in) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
            if (certificates.isEmpty()) {
                throw new IllegalArgumentException("expected non-empty set of trusted certificates");
            }

            // Put the certificates a key store.
            char[] password = "password".toCharArray(); // Any password will work.
            KeyStore keyStore = newEmptyKeyStore(password);
            int index = 0;
            for (Certificate certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificate);
            }

            // Wrap it up in an SSL context.
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, password);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    private KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream in = null; // By convention, 'null' creates an empty key store.
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}
