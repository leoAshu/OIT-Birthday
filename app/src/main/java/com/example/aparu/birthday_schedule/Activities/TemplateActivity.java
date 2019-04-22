package com.example.aparu.birthday_schedule.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aparu.birthday_schedule.API.GoogleClient;
import com.example.aparu.birthday_schedule.API.RetrofitClient;
import com.example.aparu.birthday_schedule.API.TemplateResponse;
import com.example.aparu.birthday_schedule.Adapters.TemplateAdapter;
import com.example.aparu.birthday_schedule.Models.Template;
import com.example.aparu.birthday_schedule.R;
import java.util.ArrayList;


public class TemplateActivity extends AppCompatActivity implements TemplateAdapter.TemplateSelect {

    TextView  select_template;
    static  ArrayList<Drawable> drawables = new ArrayList<>();
    static ArrayList<Template> templates = new ArrayList<>();

    TemplateAdapter.TemplateSelect templateSelect;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);

        String type = getIntent().getStringExtra("type");
        int id = 0;

        select_template = findViewById(R.id.select_template);

        templateSelect = this;

        recyclerView = findViewById(R.id.recyclerView);

        String date = getIntent().getStringExtra("date");

        if(getIntent().getStringExtra("Edit")!=null) {

            type = "edit";
            id = getIntent().getIntExtra("ScheduleId",0);
            TemplateAdapter adapter = new TemplateAdapter(drawables, getApplicationContext(), type, id, date, templateSelect);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }else{

            ArrayList<Integer> empIds = getIntent().getIntegerArrayListExtra("empIds");
            TemplateAdapter adapter = new TemplateAdapter(drawables, getApplicationContext(), type, empIds, date, templateSelect);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }

    }

    public void logout(){

        GoogleClient.getInstance(this).getClient().signOut();
        Intent intent = new Intent(TemplateActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void changeActivity(String type, int id, int temp) {

        Intent intent = new Intent(TemplateActivity.this, Message_Activity.class);
        intent.putExtra("type",type);
        intent.putExtra("id",id);
        intent.putExtra("temp",temp);
        startActivity(intent);
        finish();
    }

    @Override
    public void changeActivity(String type, int temp, ArrayList<Integer> empIds, String date) {

        Intent intent = new Intent(TemplateActivity.this, Message_Activity.class);
        intent.putExtra("type",type);
        intent.putExtra("temp",temp);
        intent.putIntegerArrayListExtra("empIds",empIds);
        intent.putExtra("date",date);
        startActivity(intent);
        finish();
    }
}
