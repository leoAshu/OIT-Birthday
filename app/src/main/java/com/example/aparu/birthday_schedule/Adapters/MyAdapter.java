package com.example.aparu.birthday_schedule.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.aparu.birthday_schedule.Models.Employee;
import com.example.aparu.birthday_schedule.R;

import java.util.ArrayList;

/**
 * Created by aparu on 2/23/2019.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {


    private Context context;
    private ArrayList<Employee> employees;
    private String type;
    ButtonStatus buttonStatus;
    static int count=0;

    public interface ButtonStatus{

        void update(int count);
    }

    public MyAdapter(ArrayList<Employee> employees, Context context, String type, ButtonStatus buttonStatus) {
        this.employees = employees;
        this.context = context;
        this.type = type;
        this.buttonStatus = buttonStatus;
    }

    public static void reset(){
        count = 0;
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if(employees.size()!=0){

            holder.user_name.setText(employees.get(position).getEmp_name());

            if(employees.get(position).getEmail_sent_status() == true || employees.get(position).getScheduled_birthday_status() == true){
                holder.status.setVisibility(View.VISIBLE);
                holder.checkBox.setVisibility(View.INVISIBLE);
            }

            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(holder.checkBox.isChecked()){

                        count++;
                        buttonStatus.update(count);
                        employees.get(position).setSelected(true);
                    }else{

                        count--;
                        buttonStatus.update(count);
                        employees.get(position).setSelected(false);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {

        return employees.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView profile_pic;
        public TextView user_name;
        public ImageView status;
        public CheckBox checkBox;


        public ViewHolder(View itemView) {
            super(itemView);

            profile_pic = itemView.findViewById(R.id.profile_pic);
            user_name = itemView.findViewById(R.id.user_name);
            status = itemView.findViewById(R.id.imageView);
            checkBox = itemView.findViewById(R.id.checkBox);

        }
    }


}
