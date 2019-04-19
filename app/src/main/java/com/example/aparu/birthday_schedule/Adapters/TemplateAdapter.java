package com.example.aparu.birthday_schedule.Adapters;

import android.app.Activity;
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

import com.example.aparu.birthday_schedule.Activities.HomeActivity;
import com.example.aparu.birthday_schedule.Activities.Message_Activity;
import com.example.aparu.birthday_schedule.Activities.TemplateActivity;
import com.example.aparu.birthday_schedule.Models.Employee;
import com.example.aparu.birthday_schedule.R;

import java.util.ArrayList;

public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.ViewHolder> {

    Context context;
    ArrayList<Drawable> templates;
    String type;
    int id;
    String date;
    ArrayList<Integer> empIds;
    TemplateSelect templateSelect;

    public interface TemplateSelect{
        void changeActivity(String type, int id, int temp);
        void changeActivity(String type, int temp, ArrayList<Integer> empIds, String date);
    }

    public TemplateAdapter(ArrayList<Drawable> templates, Context context, String type, ArrayList<Integer> empIds, String date,TemplateSelect templateSelect) {

        this.templates = templates;
        this.context = context;
        this.type = type;
        this.date = date;
        this.empIds = empIds;
        this.templateSelect = templateSelect;
    }

    public TemplateAdapter(ArrayList<Drawable> templates, Context context, String type, int id, String date, TemplateSelect templateSelect) {

        this.templates = templates;
        this.context = context;
        this.type = type;
        this.id = id;
        this.date = date;
        this.templateSelect = templateSelect;
        Log.i("type",type);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View templateList = layoutInflater.inflate(R.layout.template_list, parent, false);
        ViewHolder viewholder = new ViewHolder(templateList);

        return viewholder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final int p1 = position*2;
        final int p2 = position*2 +1;
        if(position*2 < templates.size()) {
            holder.imageView1.setImageDrawable(templates.get(p1));
            holder.imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(type.equalsIgnoreCase("edit")){

                        templateSelect.changeActivity(type, id, p1);

                    }else{

                        templateSelect.changeActivity(type, p1, empIds, date);
                    }
                }
            });
        }
        if((position*2 + 1) < templates.size()) {
            holder.imageView2.setImageDrawable(templates.get(p2));
            holder.imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(type.equalsIgnoreCase("edit")){

                        templateSelect.changeActivity(type, id, p2);

                    }else{

                        templateSelect.changeActivity(type, p2, empIds, date);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {

        return templates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView1;
        public ImageView imageView2;


        public ViewHolder(View itemView) {
            super(itemView);

            imageView1 = itemView.findViewById(R.id.imageView1);
            imageView2 = itemView.findViewById(R.id.imageView2);

        }
    }
}
