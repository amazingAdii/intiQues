package com.maddy.adiii.interviewquestion;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;

//TODO: put onclick on finish task n unfinish.(DONE)
//TODO: get data into spinner and add functionality.
//TODO: contatc us n about us page.
//TODO: add empty view.

public class MainActivity extends AppCompatActivity {
	MyHelper myDb;

	private MaterialCardView finishCardview, unFinishCardview;
	//----------------
	private RecyclerView db_todo_list;
	private LinearLayout emptyViewContainer;
	private adapter adapter;
	ArrayList<ExampleItem> exampleItems;
	//---------------
	private FloatingActionButton addNewBtn;
	public TextView listshowbtn;
	private SharedPreferences sharedPreferences;

	private Spinner allSpinnerList;
	private ArrayList<modelTaskList> tasklist;
    private int selectedSpinnerId=0;
	private int isAll=0;
    private int type=1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		myDb = new MyHelper(this);

		emptyViewContainer=(LinearLayout)findViewById(R.id.empty_view_container);
		//---------------------
		allSpinnerList=(Spinner)findViewById(R.id.all_Spinner_List);

        tasklist=new ArrayList<>();
        getDataToSpinner();

        ArrayAdapter<modelTaskList> spinnerAdapter=new ArrayAdapter<modelTaskList>(
                this,
                R.layout.spinner_item,
                tasklist
        );

        allSpinnerList.setAdapter(spinnerAdapter);

		allSpinnerList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                modelTaskList  task =(modelTaskList) adapterView.getSelectedItem();
                //       Toast.makeText(AddNewTodo.this, String.valueOf(task.getId()), Toast.LENGTH_SHORT).show();

                selectedSpinnerId=task.getId();
				if(selectedSpinnerId==0){
					isAll=0;
				}else{
					isAll=1;
				}
				viewAll(type,isAll,selectedSpinnerId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


//		Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar1);
//		setSupportActionBar(toolbar);

		//---------cardview finish n unfinish task onclick
		finishCardview =(MaterialCardView)findViewById(R.id.finish_cardview);
		unFinishCardview =(MaterialCardView)findViewById(R.id.unfinish_cardview);
		finishCardview.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				type=0;
				finishCardview.setStrokeColor(getResources().getColor(R.color.colorPrimary));
				unFinishCardview.setStrokeColor(getResources().getColor(R.color.whiteColor));
				viewAll(type,isAll,selectedSpinnerId);
			}
		});
		unFinishCardview.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				type=1;
				finishCardview.setStrokeColor(getResources().getColor(R.color.whiteColor));
				unFinishCardview.setStrokeColor(getResources().getColor(R.color.colorPrimary));
				viewAll(type,isAll,selectedSpinnerId);
			}
		});

		//---------------------
		sharedPreferences = getSharedPreferences("mypref",MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit(); // to avoid repeated values to be inserted when call the oncreate database .

		if(!sharedPreferences.contains("isFirstTime")){
			editor.putBoolean("isFirstTime", false);
			editor.commit();

			myDb.insertTaskData("Default");
			myDb.insertTaskData("Personal");
			myDb.insertTaskData("Shopping");
			myDb.insertTaskData("Work");

			Toast.makeText(this, "First time data added", Toast.LENGTH_SHORT).show();
		}

		//-------------------
		exampleItems=new ArrayList<>();
		//exampleItems.add(new ExampleItem("Line 1","Line 2"));
		//-------------------

		db_todo_list = (RecyclerView)findViewById(R.id.recycleView);
		LinearLayoutManager llm = new LinearLayoutManager(this);
		llm.setOrientation(RecyclerView.VERTICAL);
		db_todo_list.setLayoutManager(llm);
		adapter = new adapter(this, exampleItems,myDb);

		viewAll(type,isAll,selectedSpinnerId);

		addNewBtn=(FloatingActionButton) findViewById(R.id.button);
		addNewBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			    open2ndActivity();
			}
		});

	}

	public void viewAll(int type,int isAll,int l_id){
		Cursor res=myDb.getAllData();
		if(type==0){
			res=myDb.finishUnfinishTaskIndecator(type,isAll,l_id);
		}else if(type==1){
			res=myDb.finishUnfinishTaskIndecator(type,isAll,l_id);
		}
		if(res.getCount()==0){
		    emptyViewContainer.setVisibility(View.VISIBLE);
		    db_todo_list.setVisibility(View.GONE);
			return ;
		}else{
			emptyViewContainer.setVisibility(View.GONE);
			db_todo_list.setVisibility(View.VISIBLE);
			StringBuffer buffer=new StringBuffer();
			exampleItems.clear();
			while(res.moveToNext()){
				buffer.append("id :"+ res.getString(0)+"\n");
                buffer.append("title :"+ res.getString(1)+"\n");
                buffer.append("dated :"+ res.getString(2)+"\n\n");

                int id = res.getInt(0);
                String title = res.getString(1);
                String dated = res.getString(2);
                int list_id = res.getInt(3);
				String status=res.getString(4);
                ExampleItem e = new ExampleItem(title, dated, id,list_id,status);
				exampleItems.add(e);

            }
			//showmsg("data",buffer.toString());
			db_todo_list.setAdapter(adapter);


		}
	}

	public void showmsg(String titlenew,String msg){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(titlenew);
        builder.setMessage(msg);
        builder.show();
    }
    public void open2ndActivity(){
		Intent intent=new Intent(this,AddNewTodo.class);
		startActivity(intent);
	}

		//----for action bar menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case R.id.tasklist_btn:
                Intent intent=new Intent(this,allshowlist.class);
                startActivity(intent);
			return true;
			case R.id.about_btn:
			    Intent intent1=new Intent(this,AboutUs.class);
			    startActivity(intent1);
				return true;
			case R.id.share_app:
				shareApp();
				return true;
			case R.id.rate_app:
				rateMe();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}

	}
    private void getDataToSpinner(){
        Cursor c=myDb.getTaskListData();
        if(c.getCount()==0){
            Toast.makeText(this, "Nothing found", Toast.LENGTH_SHORT).show();
        }else{
            tasklist.clear();
            tasklist.add(new modelTaskList(0,"All"));
            while(c.moveToNext()){
                int id = c.getInt(0);
                String title = c.getString(1);
                modelTaskList m = new modelTaskList(id,title);
                tasklist.add(m);
            }
        }
    }

    public void rateMe(){
	    try{
	        startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id="+"com.android.crome")));

        } catch (ActivityNotFoundException e){
	        startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
        }
	}

	public void shareApp(){
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String shareBody = "I found an interesting app on play store which allows you to manage your tasks smartly. \n https://play.google.com/store/apps/details?id="+getPackageName();
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Memorize");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		startActivity(Intent.createChooser(sharingIntent, "Share via"));
	}
}
