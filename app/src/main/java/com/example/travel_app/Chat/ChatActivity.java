package com.example.travel_app.Chat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travel_app.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    private OpenAiApi openAiApi;
private Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        openAiApi = RetrofitClient.getClient().create(OpenAiApi.class);

        EditText messageInput = findViewById(R.id.messageInput);
        Button sendButton = findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userMessage = messageInput.getText().toString();
                if (!userMessage.isEmpty()){
                    sendMessageToChatBot(userMessage);
                    messageInput.setText("");
                }
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userMessage = messageInput.getText().toString();
                if (!userMessage.isEmpty()) {
                    sendMessageToChatBot(userMessage);
                    messageInput.setText("");
                }
            }
        });
    }

    private void sendMessageToChatBot(String userMessage) {
        Log.d("ChatBot", "Message Sent: " + userMessage);  // Kiểm tra xem hàm có được gọi không
        toast.makeText(this, "Message Sent: " + userMessage, Toast.LENGTH_SHORT).show();
        // Tạo tin nhắn
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("system", "You are a helpful assistant."));
        messages.add(new Message("user", userMessage));

        // Tạo yêu cầu
        ChatRequest chatRequest = new ChatRequest("gpt-3.5-turbo", messages);

        // Gửi yêu cầu qua API
        openAiApi.getChatResponse(chatRequest).enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String botReply = response.body().getChoices().get(0).getMessage().getContent();
                    Log.d("ChatBot", "Bot Reply: " + botReply);
                } else {
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string(); // Đọc lỗi từ errorBody
                            Log.e("ChatBot", "Error: " + errorBody);
                        } else {
                            Log.e("ChatBot", "Error: Unknown error occurred.");
                        }
                    } catch (Exception e) {
                        Log.e("ChatBot", "Error reading errorBody: " + e.getMessage(), e);
                    }
                }
            }


            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                Log.d("ChatBot", "Request JSON: " + new Gson().toJson(chatRequest));
                Log.e("ChatBot", "Failure: " + t.getMessage());
            }
        });
    }
}
