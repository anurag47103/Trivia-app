package com.learningandroid.trivia.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.learningandroid.trivia.controller.AppController;
import com.learningandroid.trivia.model.Question;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    ArrayList<Question> questionArrayList = new ArrayList<>();
    String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public List<Question> getQuestion(final AnswerListAsyncResponse callback) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET , url , null,
                response -> {
                    for(int i=0 ; i<response.length() ; i++) {
                        try {
                            String question = response.getJSONArray(i).getString(0);
                            boolean answer = response.getJSONArray(i).getBoolean(1);
                            questionArrayList.add(new Question(question , answer));
//                            Log.d("repo", "getQuestion: " + questionArrayList);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if(callback != null) {
                        callback.processFinished(questionArrayList);
                    }
                },
                error -> Log.d("repo", "failed_gracefully"));

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return questionArrayList;
    }
}
