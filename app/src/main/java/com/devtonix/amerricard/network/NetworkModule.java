package com.devtonix.amerricard.network;

import android.content.Context;
import android.util.Log;

import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.storage.SharedHelper;
import com.devtonix.amerricard.ui.activity.MainActivity;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    @Inject
    SharedHelper sharedHelper;

    private static final String BASE_URL_1 = "http://188.226.178.46:8888/amerricards/api/";
    private static final String BASE_URL_2 = "http://67.205.182.69:8080/amerricards/api/";

    public static final String CATEGORY_SUFFIX = "category/";
    public static final String CARD_SUFFIX = "card/";
    public static final String EVENT_SUFFIX = "event/";
    public static final String UNKNOWN_ERROR = "unknown error";


    @Provides
    @Singleton
    final API provideNetworkService(Context context) {
        ACApplication.getMainComponent().inject(this);
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d(NetworkModule.class.getSimpleName(), message);
            }
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .readTimeout(100, TimeUnit.SECONDS)
                .connectTimeout(2, TimeUnit.MINUTES)
                .build();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(sharedHelper.getCurrentServer() ? BASE_URL_1 : BASE_URL_2)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(API.class);
    }


    /** Ignore it. this for testing only. Use it in case our certificate is not valid */
//    private static OkHttpClient getUnsafeOkHttpClient() {
//        try {
//            // Create a trust manager that does not validate certificate chains
//            final TrustManager[] trustAllCerts = new TrustManager[] {
//                    new X509TrustManager() {
//                        @Override
//                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
//                                                       String authType) throws CertificateException {
//                        }
//
//                        @Override
//                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
//                                                       String authType) throws CertificateException {
//                        }
//
//                        @Override
//                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                            return new java.security.cert.X509Certificate[]{};
//                        }
//                    }
//            };
//
//            final SSLContext sslContext = SSLContext.getInstance("SSL");
//            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
//            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
//
//            OkHttpClient.Builder builder = new OkHttpClient.Builder();
//            builder.sslSocketFactory(sslSocketFactory);
//            builder.hostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String hostname, SSLSession session) {
//                    return true;
//                }
//            });
//
//            OkHttpClient okHttpClient = builder.build();
//            return okHttpClient;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
}
