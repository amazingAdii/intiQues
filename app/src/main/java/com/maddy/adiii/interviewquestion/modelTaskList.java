package com.maddy.adiii.interviewquestion;

public class modelTaskList {
    private int id;
    private String taskTitle;
    
    public modelTaskList(){
    
    }

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
    
//    @Override
//    public boolean equals(Object obj) {
//        if(obj instanceof  modelTaskList){
//            modelTaskList m = (modelTaskList) obj;
//            return m.getId() == this.id;
//        }else{
//            return false;
//        }
//    }
}
