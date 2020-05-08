package com.maddy.adiii.interviewquestion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddNewTodo extends AppCompatActivity{
    //TODO: on tasklist new element add no update shown . means add refresh.
    //TODO: change design of new tasklist dialog
    //TODO: remove blank button and add onclick on due date edittext (DONE)
    //TODO: check conditon for blank task . mhnje? without task the tasklist is getting added.(DONE)
    //TODO: increase margin on floating actionbar
   MyHelper myDb;
    public EditText dtxt,did;
    String title1;
    String date1;
    CheckBox checkStatus;
    public TextView add_new_list; //for adding the new list name.

    ArrayList<ExampleItem> TODOList;
    ArrayList<modelTaskList> tasklist;
    private DatePickerDialog.OnDateSetListener dpDialog;
    private Calendar calendar;
    public FloatingActionButton savedetailsbtn;
    private Spinner spinnerTaskList;

    private int selectedSpinnerId;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_todo);

        //TODO:backarrow on action bar with title new task
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView_new);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);





        myDb=new MyHelper(this);

        dtxt=(EditText) findViewById(R.id.dtxt);
        did=(EditText)findViewById(R.id.did);

//        db=new MyHelper(this);




        //-----------for add new list name
        add_new_list=(TextView) findViewById(R.id.add_new_list);
        add_new_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(AddNewTodo.this);
               //final AlertDialog dialog;
            View view1=getLayoutInflater().inflate(R.layout.dialog_input_text,null);
                builder.setView(view1);
               final AlertDialog dialog=builder.create();
            final EditText addnewlist=(EditText)view1.findViewById(R.id.newListName);
            final Button newlistbutton=(Button)view1.findViewById(R.id.newListButton);
            final Button cancelButton=(Button)view1.findViewById(R.id.cancelButton);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            newlistbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String titletask= addnewlist.getText().toString();
                    if(titletask.isEmpty()){
                        Toast.makeText(AddNewTodo.this, "Enter something", Toast.LENGTH_SHORT).show();
                    }else{
                        myDb.insertTaskData(titletask);
                        dialog.dismiss();

                    }
                }

            });

            dialog.show();
            }
        });


        //TODO: change button .
        savedetailsbtn=(FloatingActionButton) findViewById(R.id.savedetailsbtn);
        savedetailsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(did.getText().toString().length()==0 || dtxt.getText().toString().length()==0){
                    did.setError("Title is required ! ");
                    dtxt.setError("Select the due date ! ");
                }else{
                    AddData();
                }

            }
        });

        //------------

        tasklist=new ArrayList<>();
        getDataToSpinner();
//        tasklist.add("Default");
//        tasklist.add("Shopping List");
//        tasklist.add("Personal");
//        tasklist.add("Office work");

        spinnerTaskList=(Spinner)findViewById(R.id.spinner_task_list);
        ArrayAdapter<modelTaskList> spinnerAdapter=new ArrayAdapter<modelTaskList>(
                this,
              R.layout.spinner_item,
                tasklist
        );

        spinnerTaskList.setAdapter(spinnerAdapter);

        spinnerTaskList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                modelTaskList  task =(modelTaskList) adapterView.getSelectedItem();
         //       Toast.makeText(AddNewTodo.this, String.valueOf(task.getId()), Toast.LENGTH_SHORT).show();
                selectedSpinnerId=task.getId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //------------


        calendar = Calendar.getInstance();
        dpDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);
                //updateLabel();
                String currentstr= DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                //EditText Dtxt=(EditText)findViewById(R.id.dtxt);
                dtxt.setText(currentstr);
            }
        };

       // dtxt=(EditText)findViewById(R.id.dtxt);
        //Button datebtn=(Button)findViewById(R.id.datebtn);
        dtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddNewTodo.this, dpDialog,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
                .get(Calendar.DAY_OF_MONTH)).show();


            }
        });
    }

    //----------get data from  sqlite to spinner

    private void getDataToSpinner(){
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

        }


    }

    //----------



    public void AddData(){
       boolean isinserted= myDb.insertdata(did.getText().toString(),dtxt.getText().toString(),selectedSpinnerId,"false");
       if(isinserted=true){
           Toast.makeText(this,"Data Inserted Successfully",Toast.LENGTH_SHORT).show();
           dtxt.setText("");
           did.setText("");
       }
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }


}
