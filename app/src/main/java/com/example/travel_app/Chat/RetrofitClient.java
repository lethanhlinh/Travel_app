package com.example.travel_app.Chat;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://api.openai.com/";

    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            // Cấu hình OkHttpClient để thêm Authorization header
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .callTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(chain -> chain.proceed(
                            chain.request()
                                    .newBuilder()
                                    .header("Authorization", "Bearer Dien khoa API vao day") // Thay YOUR_API_KEY bằng API Key thực tế
                                    .header("Content-Type", "application/json")
                                    .build()
                    ))
                    .build();

            // Khởi tạo Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
