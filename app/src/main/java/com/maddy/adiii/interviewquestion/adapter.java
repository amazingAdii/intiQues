package com.maddy.adiii.interviewquestion;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


    public class adapter extends RecyclerView.Adapter<adapter.MyViewHolder>{

        private Context context;
        private List<ExampleItem> todo_list;
        private MyHelper myDb;

        public adapter(Context context, List<ExampleItem> todo_list,MyHelper myDb) {
            this.context = context;
            this.todo_list = todo_list;
            this.myDb=myDb;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(context).inflate(R.layout.example_item, viewGroup, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int position) {
            final ExampleItem user = todo_list.get(position);
            myViewHolder.Title.setText(user.getTtxt());
            myViewHolder.Dated.setText(user.getDtxt());
            String dueDate_text=user.getDtxt();
            try {
                SimpleDateFormat format=new SimpleDateFormat("EEEE, MMM dd yyyy");
                Date dueDate=format.parse(dueDate_text);
                Date currentDate=new Date();
                String cureentDateText=format.format(currentDate);
                currentDate=format.parse(cureentDateText);
                if(currentDate.after(dueDate)){
                    myViewHolder.overDueText.setVisibility(View.VISIBLE);
                }else{
                    myViewHolder.overDueText.setVisibility(View.GONE);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(user.getCheck_status().equals("true")){
                myViewHolder.check_status.setChecked(true);
                //myViewHolder.Dated.setPaintFlags(myViewHolder.Dated.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                myViewHolder.Title.setPaintFlags(myViewHolder.Title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }else if(user.getCheck_status().equals("false")){
                myViewHolder.check_status.setChecked(false);
                //myViewHolder.Dated.setPaintFlags(myViewHolder.Dated.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

                myViewHolder.Title.setPaintFlags(myViewHolder.Title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
            myViewHolder.check_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if(isChecked){
                        myDb.changedStatus(user.getId(),0);
                        //myViewHolder.Dated.setPaintFlags(myViewHolder.Dated.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        myViewHolder.Title.setPaintFlags(myViewHolder.Title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    }else{
                        myDb.changedStatus(user.getId(),1);
                        myViewHolder.Title.setPaintFlags(myViewHolder.Title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                        //myViewHolder.Dated.setPaintFlags(myViewHolder.Dated.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

                    }
                }
            });

            myViewHolder.cardview_Id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context,editEntry.class);
                    intent.putExtra("id",user.getId());
                    context.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return todo_list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{

            public TextView Title,Dated;
            public CheckBox check_status;
            public CardView cardview_Id;
            public TextView overDueText;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                cardview_Id=(CardView)itemView.findViewById(R.id.cardview_Id);
                Title= (TextView)itemView.findViewById(R.id.Ttxt);
                Dated = (TextView)itemView.findViewById(R.id.Dtxt);
                check_status=(CheckBox)itemView.findViewById(R.id.check_status);
                overDueText=(TextView)itemView.findViewById(R.id.overdue_text);
            }
        }

    }
