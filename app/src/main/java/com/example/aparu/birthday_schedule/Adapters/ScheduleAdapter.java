package com.example.aparu.birthday_schedule.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aparu.birthday_schedule.Activities.Scheduled_Wishes;
import com.example.aparu.birthday_schedule.Activities.TemplateActivity;
import com.example.aparu.birthday_schedule.Models.Employee;
import com.example.aparu.birthday_schedule.Models.Template;
import com.example.aparu.birthday_schedule.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder>{

    ArrayList<Employee> employees;
    Context context;
    ArrayList<Drawable> drawables;
    ArrayList<Template> templates;

    EditCallBack callBack;

    public interface EditCallBack{
        void edit(int id);
    }

    public ScheduleAdapter(ArrayList<Employee> employees, Context context, ArrayList<Drawable> drawables, ArrayList<Template> templates, EditCallBack callBack){
        Log.i("Adapter Size",""+employees.size());
        this.employees = employees;
        this.context = context;
        this.templates = templates;
        this.drawables = drawables;
        this.callBack = callBack;
    }

    @Override
    public ScheduleAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem;
        ScheduleAdapter.ViewHolder viewHolder;
        listItem = layoutInflater.inflate(R.layout.schedule_list, viewGroup, false);
        viewHolder = new ScheduleAdapter.ViewHolder(listItem);

        return viewHolder;
    }

    public int getPosition(int id){

        for(int i=0; i<templates.size(); i++){
            if(templates.get(i).getId() == id)
                return i;
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(ScheduleAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.user_name.setText(employees.get(i).getEmp_name()+"'s Birthday");
        viewHolder.template.setImageDrawable(drawables.get(getPosition(employees.get(i).getTemplate_id())));
        //Log.i("AdapterSchedule",""+drawables.size());
        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.edit(employees.get(i).getId());
            }
        });

    }


    @Override
    public int getItemCount() {
        return employees.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView user_name;
        public ImageView template;
        public Button edit;


        public ViewHolder(View itemView) {
            super(itemView);

            user_name = itemView.findViewById(R.id.name);
            template = itemView.findViewById(R.id.template);
            edit = itemView.findViewById(R.id.edit);

        }
    }
}
