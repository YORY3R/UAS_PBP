package com.yory3r.e_learning.models.course;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CourseResponse
{
    private String message;

    @SerializedName("data")
    private List<Course> courseList;

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public List<Course> getCourseList()
    {
        return courseList;
    }

    public void setCourseList(List<Course> courseList)
    {
        this.courseList = courseList;
    }
}
