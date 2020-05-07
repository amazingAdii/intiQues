package com.maddy.adiii.interviewquestion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class allshowlist extends AppCompatActivity {

    MyHelper myDb;
    private adaptertasklist adaptertasklist;
    private RecyclerView db_todo_task_list;
    ArrayList<modelTaskList> tasklist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allshowlist);
        myDb = new MyHelper(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tasklist=new ArrayList<>();
        db_todo_task_list = (RecyclerView)findViewById(R.id.recycleView222);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.VERTICAL);
        db_todo_task_list.setLayoutManager(llm);
        adaptertasklist=new adaptertasklist(this,tasklist,myDb);
        getDataFromSpinner();



    }


    private void getDataFromSpinner(){
        Cursor c=myDb.getTaskListData();
        if(c.getCount()==0){
            Toast.makeText(this, "Nothing found", Toast.LENGTH_SHORT).show();
        }else{
            tasklist.clear();
            while(c.moveToNext()){
                int id = c.getInt(0);
                String title = c.getString(1);
                modelTaskList m = new modelTaskList(id,title);
                tasklist.add(m);
            }
            db_todo_task_list.setAdapter(adaptertasklist);

        }

    }


    //noooo
    @Override
    protected void onResume() {
        super.onResume();
        getDataFromSpinner();
    }
}
