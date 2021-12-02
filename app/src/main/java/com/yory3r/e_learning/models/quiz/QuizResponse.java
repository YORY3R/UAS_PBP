package com.yory3r.e_learning.models.quiz;

import com.google.gson.annotations.SerializedName;
import com.yory3r.e_learning.models.course.Course;

import java.util.List;

public class QuizResponse
{
    private String message;

    @SerializedName("data")
    private List<Quiz> quizList;

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public List<Quiz> getQuizList()
    {
        return quizList;
    }

    public void setQuizList(List<Quiz> quizList)
    {
        this.quizList = quizList;
    }
}
