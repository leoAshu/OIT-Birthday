package com.example.aparu.birthday_schedule.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aparu.birthday_schedule.Activities.TemplateActivity;
import com.example.aparu.birthday_schedule.Models.Employee;
import com.example.aparu.birthday_schedule.Models.ListItem;
import com.example.aparu.birthday_schedule.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by aparu on 2/23/2019.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {


    private Context context;
    private ArrayList<Employee> employees;
    private String type;
    private String date;

    public MyAdapter(ArrayList<Employee> employees, Context context, String type, String date) {
        this.employees = employees;
        this.context = context;
        this.type = type;
        this.date = date;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem;
        ViewHolder viewHolder;
        if(type.equalsIgnoreCase("MakeWish")){

            listItem = layoutInflater.inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder(listItem);
        }else{
            listItem = layoutInflater.inflate(R.layout.list_item2, parent, false);
            viewHolder = new ViewHolder(listItem);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        if(employees.size()!=0){

            holder.user_name.setText(employees.get(position).getEmp_name());

            Log.i("",employees.get(position).getEmp_name());
            Log.i("Status",employees.get(position).getEmail_sent_status().toString());

            if(employees.get(position).getEmail_sent_status() == true || employees.get(position).getScheduled_birthday_status() == true){
                holder.status.setVisibility(View.VISIBLE);
                holder.make_wish.setVisibility(View.INVISIBLE);
            }


            holder.make_wish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(v.getContext(), "Click on Item: " + employees.get(position).getEmp_name(), Toast.LENGTH_SHORT).show();
                    Log.i("Emp-id",""+employees.get(position).getId());
                    Intent template = new Intent(context, TemplateActivity.class);
                    template.putExtra("type",type);
                    template.putExtra("id",employees.get(position).getId());
                    template.putExtra("date",date);
                    context.startActivity(template);

                }
            });
            if(employees.get(position).getEmp_name().equalsIgnoreCase("No Birthdays")){

                holder.make_wish.setEnabled(false);
            }
        }
    }

    @Override
    public int getItemCount() {

        return employees.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView profile_pic;
        public TextView user_name;
        public Button make_wish;
        public  ImageView status;


        public ViewHolder(View itemView) {
            super(itemView);

            profile_pic = itemView.findViewById(R.id.profile_pic);
            user_name = itemView.findViewById(R.id.user_name);
            make_wish = itemView.findViewById(R.id.make_wish);
            status = itemView.findViewById(R.id.imageView);

        }
    }

}
