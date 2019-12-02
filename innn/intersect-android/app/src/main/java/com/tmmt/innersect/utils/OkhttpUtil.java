package com.tmmt.innersect.utils;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.socks.library.KLog;
import com.tmmt.innersect.App;
import com.tmmt.innersect.common.AccountInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by flame on 2017/7/5.
 */

public class OkhttpUtil {

    private static final long CACHE_SIZE = 1024 * 1024 * 100;

    private static Request addHeader(Request request) {
        AccountInfo account = AccountInfo.getInstance();
        final Buffer buffer = new Buffer();
        String body = null;
        try {
            if (request.body() != null) {
                request.body().writeTo(buffer);
                body = buffer.readUtf8();
            }
        } catch (IOException e) {
            KLog.d(e);
        }
        String path = request.url().url().getFile();
        String sign = Util.getSign(request.method(), path, body);

        Request req;
        Request.Builder builder = request.newBuilder()
                .header("Accept", "application/json")
                .header("app", "innersect")
                .header("channel", SystemUtil.getChannel())
                .header("version", SystemUtil.getPackageInfo().versionName)
                .header("os", "android" + SystemUtil.getSystemVersion())
                .header("device", SystemUtil.getSystemModel())
                .header("timezone", SystemUtil.getTimeZone())
                .header("country", " ")
                .header("language", SystemUtil.getSystemLanguage())
                .header("User-Agent", "innersect")
                .header("network", SystemUtil.getNetWorkType(App.getAppContext()))
                .header("session-id", SystemUtil.getIMEI())
                .header("X-Request-sign", sign)
                .header(Util.source ==null?"nodata" :"source",Util.source==null?"no":Util.source);
        if (AccountInfo.getInstance().isLogin()) {
            req = builder.header("session-token", account.getToken()).build();
        } else {
            req = builder.build();
        }
        return req;
    }

    private static Interceptor getInterceptor() {
        final Handler handler = new Handler(Looper.getMainLooper());
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                long t1 = System.nanoTime();
                Request req = addHeader(request);
                KLog.i(String.format("Sending request %s%s", request.url(), req.headers()));
                Response response = chain.proceed(req);
                if (response.code() == 401) {
                    AccountInfo.getInstance().logout();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(App.getAppContext(), "重复登录，应用已下线", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                long t2 = System.nanoTime();
                KLog.i(String.format(Locale.getDefault(), "Received response for %s in %.1fms%n%s",
                        response.request().url(), (t2 - t1) / 1e6d, response.headers()));
                return response;
            }
        };
    }

//    public static OkHttpClient getOkhttpClient(){
//        Cache cache = new Cache(new File(App.getAppContext().getCacheDir(), "HttpCache"),CACHE_SIZE);
//
//        try {
//            final InputStream inputStream = App.getAppContext().getAssets().open("app_innersect_api.crt"); // 得到证书的输入流
//            X509TrustManager trustManager = trustManagerForCertificates(inputStream);//以流的方式读入证书
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//            sslContext.init(null, new TrustManager[]{trustManager}, null);
//            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
//
//            return new OkHttpClient.Builder().cache(cache)
//                    .connectTimeout(60, TimeUnit.SECONDS)
//                    .readTimeout(60, TimeUnit.SECONDS)
//                    .writeTimeout(60, TimeUnit.SECONDS)
//                    .sslSocketFactory(sslSocketFactory,trustManager)
//                    .addInterceptor(getInterceptor())
//                    .build();
//
//        } catch (GeneralSecurityException e) {
//            KLog.i(e);
//            throw new RuntimeException(e);
//
//        } catch (IOException e) {
//            KLog.i(e);
//            e.printStackTrace();
//        }
//        return null;
//    }

    public static OkHttpClient getOkhttpClient() {
        Cache cache = new Cache(new File(App.getAppContext().getCacheDir(), "HttpCache"), CACHE_SIZE);
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            final InputStream certificate = App.getAppContext().getAssets().open("server.cer"); // 得到证书的输入流
            keyStore.setCertificateEntry("0", certificateFactory.generateCertificate(certificate));
            try {
                if (certificate != null)
                    certificate.close();
            } catch (IOException e) {
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            //mOkHttpClient.setSslSocketFactory(sslContext.getSocketFactory());
            return new OkHttpClient.Builder().cache(cache)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .addInterceptor(getInterceptor())
                    //.sslSocketFactory(sslContext.getSocketFactory())
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 以流的方式添加信任证书
     */
    /**
     * Returns a trust manager that trusts {@code certificates} and none other. HTTPS services whose
     * certificates have not been signed by these certificates will fail with a {@code
     * SSLHandshakeException}.
     * <p>
     * <p>This can be used to replace the host platform's built-in trusted certificates with a custom
     * set. This is useful in development where certificate authority-trusted certificates aren't
     * available. Or in production, to avoid reliance on third-party certificate authorities.
     * <p>
     * <p>
     * <h3>Warning: Customizing Trusted Certificates is Dangerous!</h3>
     * <p>
     * <p>Relying on your own trusted certificates limits your server team's ability to update their
     * TLS certificates. By installing a specific set of trusted certificates, you take on additional
     * operational complexity and limit your ability to migrate between certificate authorities. Do
     * not use custom trusted certificates in production without the blessing of your server's TLS
     * administrator.
     */
    private static X509TrustManager trustManagerForCertificates(InputStream in)
            throws GeneralSecurityException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
        if (certificates.isEmpty()) {
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
        }

        // Put the certificates a key store.
        char[] password = "password".toCharArray(); // Any password will work.
        KeyStore keyStore = newEmptyKeyStore(password);
        int index = 0;
        for (java.security.cert.Certificate certificate : certificates) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }

        // Use it to build an X509 trust manager.
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }

    /**
     * 添加password
     *
     * @param password
     * @return
     * @throws GeneralSecurityException
     */
    private static KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType()); // 这里添加自定义的密码，默认
            InputStream in = null; // By convention, 'null' creates an empty key store.
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }


}
