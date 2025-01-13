package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    public void handleStartQuizButtonClick(View view) {
        Difficulty quizDifficulty = Difficulty.any;
        Spinner difficultySpinner = findViewById(R.id.quizSettingsDifficultySelectSpinner);
        String selectedDifficulty = difficultySpinner.getSelectedItem().toString().toLowerCase();

        if (selectedDifficulty.isEmpty()) {
            return;
        }

        switch (selectedDifficulty) {
            case "easy":
                quizDifficulty = Difficulty.easy;
                break;
            case "medium":
                quizDifficulty = Difficulty.medium;
                break;
            case "hard":
                quizDifficulty = Difficulty.hard;
                break;
        }

        EditText numQuestionsInput = findViewById(R.id.quizSettingsNumQuestionsInput);
        String numQuestionsText = numQuestionsInput.getText().toString();

        if (numQuestionsText.isEmpty()) {
            numQuestionsInput.setError("Please enter a number");
            return;
        }

        int quizNumQuestions;
        try {
            quizNumQuestions = Integer.parseInt(numQuestionsText);
        } catch (NumberFormatException e) {
            numQuestionsInput.setError("Invalid number");
            return;
        }

        if (quizNumQuestions <= 0 || quizNumQuestions > 25) {
            numQuestionsInput.setError(getString(R.string.num_questions_input_error));
            return;
        }

        Intent intent = new Intent(this, QuizPageActivity.class);
        intent.putExtra("difficulty", quizDifficulty.toString());
        intent.putExtra("numQuestions", quizNumQuestions);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}