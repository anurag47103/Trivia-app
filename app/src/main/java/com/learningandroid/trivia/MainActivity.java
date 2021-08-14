package com.learningandroid.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.snackbar.Snackbar;
import com.learningandroid.trivia.data.AnimationAsyncResponse;
import com.learningandroid.trivia.data.AnswerListAsyncResponse;
import com.learningandroid.trivia.data.Pref;
import com.learningandroid.trivia.data.Repository;
import com.learningandroid.trivia.databinding.ActivityMainBinding;
import com.learningandroid.trivia.model.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int currentQuestionIndex = 0;
    private int score;
    private final int total_question = 30;
    private int questionCount;
    private boolean visited[];
    private List<Question> questionList;
    private Pref pref;
    private Random random;
    private boolean finished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_main);
        pref = new Pref(MainActivity.this);
        random = new Random();

        score = pref.getScore();
        questionCount = pref.getQuestionNumber();



        questionList = new Repository().getQuestion(new AnswerListAsyncResponse() {
                    @Override
                    public void processFinished(ArrayList<Question> questionArrayList) {
                        visited = new boolean[questionArrayList.size()+1];
                        updateEverything();
                        updateHighScoreBoard();
                    }
                });

        binding.buttonTrue.setOnClickListener(view -> {
            if(!finished) {
                checkAnswer(true);
            }
        });

        binding.buttonFalse.setOnClickListener(view -> {
            if(!finished) {
                checkAnswer(false);
            }
        });

    }

    private void updateEverything() {
        updateIndex();
        updateQuestion();
        updateScoreBoard();
    }

    private void finalScore(int score) {
            binding.finalScore.setText(String.format("%s%d", getString(R.string.final_score), score));
            binding.finalScore.setVisibility(View.VISIBLE);
            pref.saveHighestScore(score);
            pref.saveScore(0);
            pref.saveQuestionNumber(1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!finished) {
            pref.saveQuestionNumber(questionCount);
            pref.saveScore(score);
        }
    }

    private void checkAnswer(boolean userAnswer) {
        boolean correctAnswer = questionList.get(currentQuestionIndex).isAnswerTrue();
        int snackMessageId;

        if(correctAnswer == userAnswer) {
            addPoints();
            visited[currentQuestionIndex] = true;
            snackMessageId = R.string.correct_answer;
            fadeAnimation();
        }
        else {
            subtractPoints();
            visited[currentQuestionIndex] = true;
            snackMessageId = R.string.wrong_answer;
            shakeAnimation();
        }

        Snackbar.make(binding.cardView , snackMessageId , Snackbar.LENGTH_SHORT).show();

        questionCount++;
        if(questionCount >= total_question) {
            finalScore(score);
            finished = true;
        }


    }

    private void subtractPoints() {
        score -= 1;
        if(score < 0) score = 0;
    }

    private void addPoints() {
        score += 4;
    }

    private void updateQuestionNumber() {
        binding.textViewOutOf.setText(String.format(getString(R.string.text_formatted), questionCount , total_question));
    }

    private void updateQuestion() {
        String question = questionList.get(currentQuestionIndex).getAnswer();
        binding.textViewQuestion.setText(question);
        updateQuestionNumber();
    }

    private void updateIndex() {
        currentQuestionIndex = random.nextInt(questionList.size());
        while(visited[currentQuestionIndex]) {
            currentQuestionIndex = random.nextInt(questionList.size());
        }
    }

    private void updateHighScoreBoard() {
        int highScore = pref.getHighestScore();
        binding.textViewHighScore.setText(String.format("%s%d", getString(R.string.highScore), highScore));
    }

    private void updateScoreBoard() {
        binding.textViewScore.setText(String.format("%s%d", getString(R.string.scoreValue), score));
    }

    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this , R.anim.shake_animation);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.textViewQuestion.setTextColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.textViewQuestion.setTextColor(Color.WHITE);
                updateEverything();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        binding.cardView.startAnimation(shake);
    }

    private void fadeAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f , 0.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.textViewQuestion.setTextColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.textViewQuestion.setTextColor(Color.WHITE);
                updateEverything();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        binding.cardView.startAnimation(alphaAnimation);
    }
}