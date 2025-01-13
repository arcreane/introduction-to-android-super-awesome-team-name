package com.example.quizapp.api;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import com.example.quizapp.models.Quiz;

public interface QuizAPI {
    @GET("api.php?")
    Call<Quiz> getQuiz(
            @Query("amount") int amount,
            @Query("type") String type,
            @Query("difficulty") String difficulty,
            @Query("encode") String encode
    );
}
