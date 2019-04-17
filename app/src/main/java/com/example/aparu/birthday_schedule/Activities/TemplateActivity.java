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

import com.example.aparu.birthday_schedule.API.RetrofitClient;
import com.example.aparu.birthday_schedule.API.TemplateResponse;
import com.example.aparu.birthday_schedule.Adapters.TemplateAdapter;
import com.example.aparu.birthday_schedule.Models.Template;
import com.example.aparu.birthday_schedule.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TemplateActivity extends AppCompatActivity {

    ImageView back_switch;
    TextView  select_template;
    static  ArrayList<Drawable> drawables = new ArrayList<>();
    static ArrayList<Template> templates = new ArrayList<>();

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);

        String type = getIntent().getStringExtra("type");
        int id = 0;

        back_switch = findViewById(R.id.back_switch);
        select_template = findViewById(R.id.select_template);

        recyclerView = findViewById(R.id.recyclerView);

        String date = getIntent().getStringExtra("date");

        if(getIntent().getStringExtra("Edit")!=null) {

            type = "edit";
            id = getIntent().getIntExtra("ScheduleId",0);
            TemplateAdapter adapter = new TemplateAdapter(drawables, getApplicationContext(), type, id, date);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }else{

            ArrayList<Integer> empIds = getIntent().getIntegerArrayListExtra("empIds");
            TemplateAdapter adapter = new TemplateAdapter(drawables, getApplicationContext(), type, empIds, date);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }


        back_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getIntent().getStringExtra("Edit")!=null){

                    Intent homeActivity = new Intent(getApplicationContext(), Scheduled_Wishes.class);
                    startActivity(homeActivity);
                    finish();
                }else{
                    Intent homeActivity = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(homeActivity);
                    finish();
                }
            }
        });

    }

    public void logout(){

        LoginActivity.googleSignInClient.signOut();
        Intent intent = new Intent(TemplateActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
