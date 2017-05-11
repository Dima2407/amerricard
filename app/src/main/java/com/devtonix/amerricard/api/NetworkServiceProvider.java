package com.devtonix.amerricard.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkServiceProvider {

    public static final String BASE_URL = "http://188.226.178.46:8888/amerricards/api/";
    public static final String CATEGORY_SUFFIX = "category/";
    public static final String CARD_SUFFIX = "card/";

    public IBackendService service;

    public NetworkServiceProvider() {
        this("");
    }

    public NetworkServiceProvider(String url) {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Interceptor headerInterceptor = new Interceptor() {
              @Override
              public Response intercept(Interceptor.Chain chain) throws IOException {
                  Request original = chain.request();

                  Request request = original.newBuilder()
                          .header("Content-Type", "application/json; charset=utf-8")
                          .method(original.method(), original.body())
                          .build();

                  return chain.proceed(request);
              }
        };

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(loggingInterceptor).addInterceptor(headerInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url.isEmpty() ? BASE_URL : url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        service = retrofit.create(IBackendService.class);
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
