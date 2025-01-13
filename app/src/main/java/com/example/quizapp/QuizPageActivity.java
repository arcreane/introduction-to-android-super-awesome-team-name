package com.example.quizapp;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.VibratorManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Vibrator;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import com.example.quizapp.models.Question;
import com.example.quizapp.api.QuizAPI;
import com.example.quizapp.api.RetrofitClient;
import com.example.quizapp.models.Quiz;

public class QuizPageActivity extends AppCompatActivity {

    private TextView questionNumberTextView, questionTextView, scoreTextView, difficultyTextView;
    private Button option1Button, option2Button, option3Button, option4Button;

    private Vibrator vibrator;

    private List<Question> questionsList = new ArrayList<>();
    private int currentQuestionIndex = 0;

    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_page);

        // Initialize views
        questionNumberTextView = findViewById(R.id.questionNumberTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        difficultyTextView = findViewById(R.id.difficultyTextView);
        questionTextView = findViewById(R.id.questionTextView);
        option1Button = findViewById(R.id.option1Button);
        option2Button = findViewById(R.id.option2Button);
        option3Button = findViewById(R.id.option3Button);
        option4Button = findViewById(R.id.option4Button);

        // Assign the Vibrator instance to the class-level variable
        VibratorManager vibratorManager = (VibratorManager) getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
        vibrator = vibratorManager.getDefaultVibrator();

        // Fetch quiz questions
        fetchQuizQuestions();
    }

    private void fetchQuizQuestions() {
        // Initialize Retrofit
        Retrofit retrofit = RetrofitClient.getClient();

        // Create API instance
        QuizAPI quizAPI = retrofit.create(QuizAPI.class);

        // Make API call
        Call<Quiz> call = quizAPI.getQuiz(10, "multiple", "url3986");
        call.enqueue(new Callback<Quiz>() {
            @Override
            public void onResponse(Call<Quiz> call, Response<Quiz> response) {
                if (response.isSuccessful() && response.body() != null) {
                    questionsList = response.body().getResults();
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
            //Update question number and score in header
            questionNumberTextView.setText(String.format("%s%d/%d", getString(R.string.question_number), currentQuestionIndex + 1, questionsList.size()));


            // Get the current question
            Question question = questionsList.get(currentQuestionIndex);

            String difficulty = question.getDifficulty();


            int question_points;

            switch (difficulty.toLowerCase()) {
                case "easy":
                    question_points = 5;
                    difficultyTextView.setTextColor(getColor(R.color.difficulty_easy));
                    break;
                case "medium":
                    question_points = 10;
                    difficultyTextView.setTextColor(getColor(R.color.difficulty_medium));
                    break;
                default:
                    question_points = 15;
                    difficultyTextView.setTextColor(getColor(R.color.difficulty_hard));
                    break;
            }

            // Set question text
            questionTextView.setText(question.getQuestion());
            String formattedDifficulty = difficulty.substring(0, 1).toUpperCase() +
                    difficulty.substring(1).toLowerCase();
            difficultyTextView.setText(String.format(getString(R.string.difficulty_label), formattedDifficulty));

            // Shuffle options (to randomize button positions)
            List<String> options = new ArrayList<>(question.getIncorrect_answers());
            options.add(question.getCorrect_answer());
            Collections.shuffle(options);

            // Set button texts
            option1Button.setText(options.get(0));
            option2Button.setText(options.get(1));
            option3Button.setText(options.get(2));
            option4Button.setText(options.get(3));

            // Set button click listeners
            setOptionButtonListeners(question.getCorrect_answer(), question_points);
        } else {
            Toast.makeText(this, "Quiz Completed!", Toast.LENGTH_LONG).show();
        }
    }

    private void setOptionButtonListeners(String correctAnswer, Integer points) {
        View.OnClickListener listener = v -> {
            Button clickedButton = (Button) v;
            String selectedAnswer = clickedButton.getText().toString();

            if (selectedAnswer.equals(correctAnswer)) {
                Toast.makeText(QuizPageActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
                score += points;
                scoreTextView.setText(String.format("%s %d", getString(R.string.quiz_score), score));
            } else {
                Toast.makeText(QuizPageActivity.this, "Wrong! Correct Answer: " + correctAnswer, Toast.LENGTH_SHORT).show();
                // Vibrate for 500 milliseconds
                if (vibrator != null && vibrator.hasVibrator()) {
                    long duration = 500; // Define the duration in milliseconds
                    vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
                    Log.d("Vibration", "Vibration triggered for wrong answer");
                }
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
