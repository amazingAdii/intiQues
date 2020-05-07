package com.maddy.adiii.interviewquestion;

public class ExampleItem {
    private String Ttxt;
    private String Dtxt;
    private int id;
    private int list_id;
    private String check_status;



    public ExampleItem(String Ttxt, String Dtxt, int id, int list_id, String check_status){
       this.Ttxt=Ttxt;
       this.Dtxt=Dtxt;
       this.id=id;
       this.list_id=list_id;
       this.check_status=check_status;
    }
    public String getCheck_status() {
        return check_status;
    }

    public void setCheck_status(String check_status) {
        this.check_status = check_status;
    }

    public int getList_id() {
        return list_id;
    }

    public void setList_id(int list_id) {
        this.list_id = list_id;
    }

    public String getTtxt() {
        return Ttxt;
    }

    public String getDtxt() {
        return Dtxt;
    }

    public void setTtxt(String ttxt) {
        Ttxt = ttxt;
    }

    public void setDtxt(String dtxt) {
        Dtxt = dtxt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
