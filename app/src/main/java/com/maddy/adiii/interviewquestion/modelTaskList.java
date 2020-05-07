package com.maddy.adiii.interviewquestion;

import androidx.annotation.NonNull;

public class modelTaskList {
    private int id;
    private String taskTitle;

    public modelTaskList(int id, String taskTitle) {
        this.id = id;
        this.taskTitle = taskTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskTitle() {
        return taskTitle;
    }



    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }


    @Override
    public String toString() {
        return taskTitle;
    }
}
