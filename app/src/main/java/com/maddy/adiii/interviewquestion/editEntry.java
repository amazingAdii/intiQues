package com.maddy.adiii.interviewquestion;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

public class editEntry extends AppCompatActivity {

    private MyHelper myDb;

    private int taskId;
    private TextView editNewList;
    private EditText editTitle,editDated;
    private Spinner spinnerEdit;
    //private EditText editDated;

    private FloatingActionButton editDetailsBtn;

    private ArrayList<modelTaskList> tasklist;
    private ArrayAdapter<modelTaskList> spinnerAdapter;
    private String status;
    private DatePickerDialog.OnDateSetListener dpDialog;
    private Calendar calendar;
    private int selectedSpinnerId;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entry);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView_new);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back janya sathi. arrow...

        Bundle extras=getIntent().getExtras();
        taskId= extras.getInt("id");
        Toast.makeText(this, String.valueOf(taskId), Toast.LENGTH_SHORT).show();

        editTitle=(EditText) findViewById(R.id.edit_title);
        editDated=(EditText) findViewById(R.id.edit_dated);
        editNewList=(TextView)findViewById(R.id.edit_new_list);
        spinnerEdit=(Spinner) findViewById(R.id.spinner_edit_task_list);
        //editDateBtn=(Button)findViewById(R.id.edit_date_btn);

        editDetailsBtn=(FloatingActionButton) findViewById(R.id.edit_details_btn);

        myDb=new MyHelper(this);

       editNewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(editEntry.this);
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
                            Toast.makeText(editEntry.this, "Enter something", Toast.LENGTH_SHORT).show();
                        }else{
                            myDb.insertTaskData(titletask);
                            dialog.dismiss();
                        }
                    }
                });

                dialog.show();
            }
        });


        tasklist=new ArrayList<>();
        getDataToSpinner();

        spinnerAdapter=new ArrayAdapter<modelTaskList>(
                this,
                android.R.layout.simple_spinner_item,
                tasklist
        );

        spinnerEdit.setAdapter(spinnerAdapter);
        setAllValues(taskId);
       spinnerEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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



        calendar = Calendar.getInstance();

        dpDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);
                //updateLabel();
                String currentstr= DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                EditText Dtxt=(EditText) findViewById(R.id.edit_dated);
                Dtxt.setText(currentstr);
            }
        };



        editDated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(editEntry.this, dpDialog,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH)).show();


            }
        });

        editDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTaskItem(taskId);
            }
        });

    }

    private void updateTaskItem(int taskId) {
        String newTitle=editTitle.getText().toString();
        String newDated=editDated.getText().toString();
        myDb.updateSingleList(taskId,newTitle,newDated,selectedSpinnerId,status);
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }


    private void setAllValues(int taskId) {
        Cursor res=myDb.getSingleListItem(taskId);
        if(res.moveToFirst()) {
            int id = res.getInt(0);
            String title = res.getString(1);
            String dated = res.getString(2);
            final int task_id = res.getInt(3);
            status = res.getString(4);

            editTitle.setText(title);
            editDated.setText(dated);
            //Toast.makeText(this, tasklist.get(task_id-1).toString(), Toast.LENGTH_SHORT).show();
            
            boolean checkElement = false;
            for(int i =0; i< tasklist.size(); i++){
                modelTaskList m = tasklist.get(i);
                if(m.getId() == task_id-1){
                    checkElement=true;
                }
            }
 
            
            if(checkElement){
                int pos = spinnerAdapter.getPosition(tasklist.get(task_id-1));
                //Toast.makeText(this, pos, Toast.LENGTH_SHORT).show();
                spinnerEdit.setSelection(pos);
                selectedSpinnerId = task_id;
            }else {
                //int pos = spinnerAdapter.getPosition(tasklist.get(task_id - 1));
                //Toast.makeText(this, pos, Toast.LENGTH_SHORT).show();
                spinnerEdit.setSelection(0);
                selectedSpinnerId = 0;
            }
        }
    }


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
    //-------for delete whole record of specific id
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){
            case R.id.edit_delete_btn:
               deleteTaskDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void deleteTaskDialog(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        myDb.deleteWholeTask(taskId);
                        Intent intent=new Intent(editEntry.this,MainActivity.class);
                        startActivity(intent);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(editEntry.this);
        builder.setTitle("Delete");
        builder.setMessage("Do you want to delete ?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

}
