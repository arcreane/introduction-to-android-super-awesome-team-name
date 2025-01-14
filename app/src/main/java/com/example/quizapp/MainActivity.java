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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final Map<String, Integer> categoryIds = new HashMap<>() {{
        put(null, 0);
        put("Any Category", 0);
        put("General Knowledge", 9);
        put("Entertainment: Books", 10);
        put("Entertainment: Film", 11);
        put("Entertainment: Music", 12);
        put("Entertainment: Musicals & Theatres", 13);
        put("Entertainment: Television", 14);
        put("Entertainment: Video Games", 15);
        put("Entertainment: Board Games", 16);
        put("Science & Nature", 17);
        put("Science: Computers", 18);
        put("Science: Mathematics", 19);
        put("Mythology", 20);
        put("Sports", 21);
        put("Geography", 22);
        put("History", 23);
        put("Politics", 24);
        put("Art", 25);
        put("Celebrities", 26);
        put("Animals", 27);
        put("Vehicles", 28);
        put("Entertainment: Comics", 29);
        put("Science: Gadgets", 30);
        put("Entertainment: Japanese Anime & Manga", 31);
        put("Entertainment: Cartoon & Animations", 32);
    }};

    public void handleStartQuizButtonClick(View view) {
        Difficulty quizDifficulty = Difficulty.any;
        Spinner difficultySpinner = findViewById(R.id.quizSettingsDifficultySelectSpinner);
        String selectedDifficulty = difficultySpinner.getSelectedItem().toString().toLowerCase();
        Spinner categorySpinner = findViewById(R.id.quizSettingsCategorySpinner);
        String selectedCategory = categorySpinner.getSelectedItem().toString();

        int categoryId = categoryIds.getOrDefault(selectedCategory, 0);

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
        intent.putExtra("categoryId", categoryId);
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