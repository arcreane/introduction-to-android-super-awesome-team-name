package com.example.quizapp.models;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Question {
    private String type;
    private String difficulty;
    private String category;
    private String question;
    private String correct_answer;
    private List<String> incorrect_answers = new ArrayList<String>();

    private String decodeString(String encoded) {
        if (encoded == null) return null;
        // Then decode it properly with URLDecoder
        return URLDecoder.decode(encoded, StandardCharsets.UTF_8);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getQuestion() {
        return decodeString(question);
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrect_answer() {
        return decodeString(correct_answer);
    }

    public void setCorrect_answer(String correct_answer) {
        this.correct_answer = decodeString(correct_answer);
    }

    public List<String> getIncorrect_answers() {
        return incorrect_answers.stream().map(this::decodeString).collect(Collectors.toList());
    }

    public void setIncorrect_answers(List<String> incorrect_answers) {
        this.incorrect_answers = incorrect_answers.stream()
                .map(this::decodeString)
                .collect(Collectors.toList());
    }
}