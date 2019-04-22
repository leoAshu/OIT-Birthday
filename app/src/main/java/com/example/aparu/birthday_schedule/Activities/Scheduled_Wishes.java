package com.example.aparu.birthday_schedule.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aparu.birthday_schedule.API.BirthdayResponse;
import com.example.aparu.birthday_schedule.API.GoogleClient;
import com.example.aparu.birthday_schedule.API.RetrofitClient;
import com.example.aparu.birthday_schedule.Activities.HomeActivity;
import com.example.aparu.birthday_schedule.Adapters.ScheduleAdapter;
import com.example.aparu.birthday_schedule.Models.Employee;
import com.example.aparu.birthday_schedule.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Scheduled_Wishes extends AppCompatActivity implements ScheduleAdapter.EditCallBack {

    TextView schedule_page_text;
    RecyclerView recyclerView;
    ImageView nothingFound;

    ArrayList<Employee> scheduleList = new ArrayList<>();
    ScheduleAdapter adapter;
    ScheduleAdapter.EditCallBack callBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduled__wishes);

        nothingFound = findViewById(R.id.nothingFound);

        String token = GoogleSignIn.getLastSignedInAccount(this).getIdToken();

        callBack = this;

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ScheduleAdapter(scheduleList,getApplicationContext(),TemplateActivity.drawables,TemplateActivity.templates, callBack);
        recyclerView.setAdapter(adapter);

        Call<BirthdayResponse> call = RetrofitClient.getInstance()
                .getApi()
                .getScheduled(token);
        call.enqueue(new Callback<BirthdayResponse>() {
            @Override
            public void onResponse(Call<BirthdayResponse> call, Response<BirthdayResponse> response) {
                if(response.code() == 200){

                    nothingFound.setVisibility(View.INVISIBLE);
                    BirthdayResponse response1 = response.body();
                    scheduleList = response1.getEmployees();

//                    Log.i("Schedule Size",""+scheduleList.size());
//                    Log.i("Schedule Name",""+scheduleList.get(0).getEmp_name());
//                    Log.i("Schedule DOB",""+scheduleList.get(0).getEmp_dob());
//                    Log.i("Schedule Template",""+scheduleList.get(0).getTemplate_id());

                    adapter = new ScheduleAdapter(scheduleList,getApplicationContext(),TemplateActivity.drawables,TemplateActivity.templates, callBack);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else if(response.code() == 404){

                    nothingFound.setVisibility(View.VISIBLE);
                }else if(response.code() == 401){

                    Toast.makeText(getApplicationContext(),"Token Expired. Please Login Again.",Toast.LENGTH_LONG).show();
                    logout();
                }
            }

            @Override
            public void onFailure(Call<BirthdayResponse> call, Throwable throwable) {

            }
        });

        schedule_page_text = findViewById(R.id.schedule_page_text);

    }
    public void logout(){

        GoogleClient.getInstance(this).getClient().signOut();
        Intent intent = new Intent(Scheduled_Wishes.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void edit(int id) {

        Intent intent = new Intent(Scheduled_Wishes.this, TemplateActivity.class);
        intent.putExtra("Edit","edit");
        intent.putExtra("ScheduleId",id);
        startActivity(intent);
        finish();
    }
}
