package com.maddy.adiii.interviewquestion;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class adaptertasklist extends RecyclerView.Adapter<adaptertasklist.MyViewHolder>{
    private Context context;
    private List<modelTaskList> task_list;
    private MyHelper myDb;
    public adaptertasklist(Context context, List<modelTaskList> task_list,MyHelper myDb) {
        this.context = context;
        this.task_list = task_list;
        this.myDb=myDb;
    }
    @NonNull
    @Override
    public adaptertasklist.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.example_task_list_item, viewGroup, false);
        return new adaptertasklist.MyViewHolder(v);


    }

    @Override
    public void onBindViewHolder(@NonNull final adaptertasklist.MyViewHolder myViewHolder, final int position) {
        final modelTaskList m=task_list.get(position);
        myViewHolder.taskListTitle.setText(m.getTaskTitle());
        if(m.getId()<5){
            myViewHolder.deleting_img.setVisibility(View.INVISIBLE);
        }
        myViewHolder.deleting_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                myDb.deleteTaskListName(m.getId());
                                task_list.remove(position);
                                notifyDataSetChanged();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Do you want to delete ?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return task_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public CardView cardview_Id_Tasklist;
        public TextView taskListTitle;
        public ImageButton deleting_img;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardview_Id_Tasklist=(CardView)itemView.findViewById(R.id.cardview_Id222);
            taskListTitle =(TextView)itemView.findViewById(R.id.editing_Text);
            deleting_img=(ImageButton)itemView.findViewById(R.id.deleting_Img);

        }

    }
    }

