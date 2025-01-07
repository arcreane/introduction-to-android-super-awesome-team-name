package com.example.quizapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.quizapp.models.Question;
import com.example.quizapp.api.QuizAPI;
import com.example.quizapp.api.RetrofitClient;
import com.example.quizapp.models.Quiz;

public class QuizPageActivity extends AppCompatActivity {

    private TextView questionTextView;
    private Button option1Button, option2Button, option3Button, option4Button;

    private List<Question> questionsList = new ArrayList<>();
    private int currentQuestionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_page);

        // Initialize views
        questionTextView = findViewById(R.id.questionTextView);
        option1Button = findViewById(R.id.option1Button);
        option2Button = findViewById(R.id.option2Button);
        option3Button = findViewById(R.id.option3Button);
        option4Button = findViewById(R.id.option4Button);

        // Fetch quiz questions
        fetchQuizQuestions();
    }

    private void fetchQuizQuestions() {
        // Initialize Retrofit
        Retrofit retrofit = RetrofitClient.getClient();

        // Create API instance
        QuizAPI quizAPI = retrofit.create(QuizAPI.class);

        // Make API call
        Call<Quiz> call = quizAPI.getQuiz(10, "9");
        call.enqueue(new Callback<Quiz>() {
            @Override
            public void onResponse(Call<Quiz> call, Response<Quiz> response) {
                if (response.isSuccessful() && response.body() != null) {
                    questionsList = response.body().results;
                    displayQuestion();
                } else {
                    Toast.makeText(QuizPageActivity.this, "Failed to load questions", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Quiz> call, Throwable t) {
                Toast.makeText(QuizPageActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("QuizAPI", "Failure: " + t.getMessage());
            }
        });
    }

    private void displayQuestion() {
        if (currentQuestionIndex < questionsList.size()) {
            // Get the current question
            Question question = questionsList.get(currentQuestionIndex);

            // Set question text
            questionTextView.setText(question.getQuestion());

            // Shuffle options (to randomize button positions)
            List<String> options = new ArrayList<>(question.getIncorrectAnswers());
            options.add(question.getCorrectAnswer());
            java.util.Collections.shuffle(options);

            // Set button texts
            option1Button.setText(options.get(0));
            option2Button.setText(options.get(1));
            option3Button.setText(options.get(2));
            option4Button.setText(options.get(3));

            // Set button click listeners
            setOptionButtonListeners(question.getCorrectAnswer());
        } else {
            Toast.makeText(this, "Quiz Completed!", Toast.LENGTH_LONG).show();
        }
    }

    private void setOptionButtonListeners(String correctAnswer) {
        View.OnClickListener listener = v -> {
            Button clickedButton = (Button) v;
            String selectedAnswer = clickedButton.getText().toString();

            if (selectedAnswer.equals(correctAnswer)) {
                Toast.makeText(QuizPageActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(QuizPageActivity.this, "Wrong! Correct Answer: " + correctAnswer, Toast.LENGTH_SHORT).show();
            }

            // Move to the next question
            currentQuestionIndex++;
            displayQuestion();
        };

        option1Button.setOnClickListener(listener);
        option2Button.setOnClickListener(listener);
        option3Button.setOnClickListener(listener);
        option4Button.setOnClickListener(listener);
    }
}
