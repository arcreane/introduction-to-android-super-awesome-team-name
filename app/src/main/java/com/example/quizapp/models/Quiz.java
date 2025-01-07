package com.example.quizapp.models;

import java.util.ArrayList;
import java.util.List;

public class Quiz {

    private Integer responseCode;
    private List<Question> results = new ArrayList<Question>();
    public Integer getResponseCode() {
        return responseCode;
    }
    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }
    public List<Question> getResults() {
        return results;
    }
    public void setResults(List<Question> results) {
        this.results = results;
    }
//    public int response_code;
//    public List<Question> results;
}
