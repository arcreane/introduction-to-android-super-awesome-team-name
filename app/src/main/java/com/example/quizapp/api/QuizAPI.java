package com.example.quizapp.api;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import com.example.quizapp.models.Quiz;

public interface QuizAPI {
    @GET("/")
    Call<Quiz> getQuiz(
            @Query("amount") int amount,
            @Query("difficulty") String difficulty
    );
}
