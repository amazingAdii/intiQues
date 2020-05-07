package com.maddy.adiii.interviewquestion;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;

public class MyHelper extends SQLiteOpenHelper {

    public static final String Database_name="todo.db";
    public static final String Table_tasklist="task_list";
    public static final String Table_name="todo_table";
   // public static final Cursor res="res";
//    private static final String dname="mydb";
//    private static final int version=1;
//    private static final String TODOlist="_TODO";
    private static final String col_2="title";
    private static final String col_3="dated";
    private static final String col_1="id";
    private static final String col_4="list_id";
    private static final String col_5="status";
    private static final String col_1_list="id";
    private static final String col_2_list="listtitle";
    public MyHelper(Context context){
        super(context,Database_name,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //String sql="CREATE TABLE IF NOT EXISTS "+TODOlist+"(" +id+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+Title+"TEXT, "+Dated+"TEXT );";
       // db.execSQL(sql);
        db.execSQL("create table if not exists "+Table_name+" (id INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT,dated TEXT,list_id INTEGER,status TEXT )");
        db.execSQL("create table if not exists "+Table_tasklist+" (id INTEGER PRIMARY KEY AUTOINCREMENT, listtitle TEXT)");

    }

    public void changedStatus(int id,int type){
        if(type==0){
            SQLiteDatabase db=this.getWritableDatabase();
            db.execSQL(" UPDATE "+Table_name+" SET status = 'true' WHERE id= "+id);
        }else if(type==1){
            SQLiteDatabase db=this.getWritableDatabase();
            db.execSQL(" UPDATE "+Table_name+" SET status = 'false' WHERE id= "+id);

        }
    }

    //+++++++++++++++++++++++++++++++++
//    public Cursor readSpinnerData(int id){
////        String selectQuery = "SELECT  * FROM " + Table_tasklist;
////        SQLiteDatabase db = this.getReadableDatabase();
////
////        Cursor cursor = db.rawQuery(selectQuery,null,null);
//        SQLiteDatabase db=this.getWritableDatabase();
//        Cursor res=db.rawQuery("select * from "+Table_tasklist,null);
//        return res;
//
//    }
//++++++++++++++++++++++
    public void insertTaskData(String taskTitle){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(col_2_list,taskTitle);
        long result=db.insert(Table_tasklist,null,contentValues);

    }
    //------delete task list name

    public void deleteTaskListName(int id){
        SQLiteDatabase db=this.getWritableDatabase();
       db.execSQL("delete from "+Table_tasklist+" where id="+id);
    }

    //------


    //------------get single element using id(get id to edit that all records)

    public Cursor getSingleListItem(int id){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("select * from "+Table_name+" where id ="+id,null);
        return res;
    }

    public void updateSingleList(int id,String title,String dated,int list_id,String status){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("update "+Table_name+" set title= \'"+title+"\',dated= \'"+dated+"\',list_id ="+list_id+",status = \'"+status+"\' where id="+id);

    }

    //------------


    public Cursor getTaskListData(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+Table_tasklist,null);
        return res;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        db.execSQL(" DROP TABLE IF EXISTS "+Table_name);
        onCreate(db);
    }
    public boolean insertdata(String title,String dated, int list_id, String status){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(col_2,title);
        contentValues.put(col_3,dated);
        contentValues.put(col_4,list_id);
        contentValues.put(col_5,status);
        long result=db.insert(Table_name,null,contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getAllData(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+Table_name,null);
        return res;

    }

    public void deleteWholeTask(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("delete from "+Table_name+" where id="+id);
    }

    public Cursor finishUnfinishTaskIndecator(int type,int isAll,int list_id){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=null;
        if(type==0){
            if(isAll==0) {
                res = db.rawQuery("select * from " + Table_name + " where status=\'true\'", null);
            }else if(isAll==1){
                res = db.rawQuery("select * from " + Table_name + " where status=\'true\' and list_id="+list_id, null);

            }
        }else if(type==1){
            if(isAll==0) {
                res = db.rawQuery("select * from " + Table_name + " where status=\'false\'", null);
            }else if(isAll==1){
                res = db.rawQuery("select * from " + Table_name + " where status=\'false\' and list_id="+list_id, null);
            }
        }

        return res;
    }

}
