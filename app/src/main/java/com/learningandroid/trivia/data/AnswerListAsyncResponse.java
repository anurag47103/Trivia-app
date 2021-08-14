package com.learningandroid.trivia.data;

import com.learningandroid.trivia.model.Question;

import java.util.ArrayList;

public interface AnswerListAsyncResponse {
    void processFinished(ArrayList<Question> questionArrayList);
}

