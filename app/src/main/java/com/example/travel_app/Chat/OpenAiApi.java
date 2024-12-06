package com.example.travel_app.Chat;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface OpenAiApi {
    // Endpoint để gửi tin nhắn
    @POST("v1/chat/completions")
    Call<ChatResponse> getChatResponse(@Body ChatRequest chatRequest);
}
