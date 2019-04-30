package com.example.aparu.birthday_schedule.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aparu.birthday_schedule.API.BirthdayResponse;
import com.example.aparu.birthday_schedule.API.GoogleClient;
import com.example.aparu.birthday_schedule.API.RetrofitClient;
import com.example.aparu.birthday_schedule.Adapters.MyAdapter;
import com.example.aparu.birthday_schedule.Models.Employee;
import com.example.aparu.birthday_schedule.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, MyAdapter.ButtonStatus {

    ImageView calender_date;
    TextView current_date;
    Button schedule_birthday;
    ImageView nothingFound;
    Button makeWish;

    private static RecyclerView recyclerView;
    private static MyAdapter adapter;
    private static ArrayList<Employee> employees = new ArrayList<>();

    String type = "MakeWish";
    Calendar today;
    String currentDate;
    String selDate;
    boolean doubleBackToExitPressedOnce = false;

    MyAdapter.ButtonStatus buttonStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("SEND WISHES");
        setSupportActionBar(toolbar);
        nothingFound = findViewById(R.id.nothingFound);
        makeWish = findViewById(R.id.makeWish);

        buttonStatus = this;

        current_date = findViewById(R.id.current_date);
        Calendar calendar = Calendar.getInstance();
        currentDate = DateFormat.getDateInstance().format(calendar.getTime());
        current_date.setText("Today's Date: " + currentDate);
        today = calendar;

        calender_date = findViewById(R.id.calender_date);

        calender_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        selDate = currentDate;

        try{

            FetchList task = new FetchList();
            task.execute();

        }catch (Exception e){

            e.printStackTrace();
        }


        employees.clear();
        HomeActivity.unSelect();
        MyAdapter.reset();

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new MyAdapter(employees, this,type, buttonStatus);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        schedule_birthday = findViewById(R.id.schedule_birthday);

        schedule_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent schedule_page = new Intent(getApplicationContext(), Scheduled_Wishes.class);
                startActivity(schedule_page);
            }
        });

    }


    @Override
    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        final String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

        makeWish.setVisibility(View.INVISIBLE);

        MyAdapter.reset();
        HomeActivity.unSelect();

        final int tDay = today.get(Calendar.DAY_OF_MONTH);
        final int tMonth = today.get(Calendar.MONTH);
        final int tYear = today.get(Calendar.YEAR);


        final String date = year+"-"+(month+1)+"-"+dayOfMonth;
        selDate = date;


        Call<BirthdayResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getBirthdays(GoogleSignIn.getLastSignedInAccount(getApplicationContext()).getIdToken(),date,date);

        Log.i("Schedule Date",date);

        call.enqueue(new Callback<BirthdayResponse>() {
            @Override
            public void onResponse(Call<BirthdayResponse> call, Response<BirthdayResponse> response) {

                String t = "Schedule";
                String d = dayOfMonth+"-"+months[month]+"-"+year;
                if (tDay == dayOfMonth && tMonth == month && tYear == year){

                    current_date.setText("Today's Date: " + d);
                    t = "MakeWish";
                }else{
                    current_date.setText("Date: "+d);
                }

                if(response.code() == 200) {

                    employees.clear();
                    HomeActivity.employees = response.body().getEmployees();
                    HomeActivity.unSelect();
                    showList(t,selDate);
                    nothingFound.setVisibility(View.INVISIBLE);

                }else if(response.code() == 404){

                    employees.clear();
                    showList(t,selDate);
                    nothingFound.setVisibility(View.VISIBLE);

                }else if(response.code() == 401){
                    Toast.makeText(getApplicationContext(),"Token Expired. Please Login Again.",Toast.LENGTH_LONG).show();
                    logout();
                }
            }

            @Override
            public void onFailure(Call<BirthdayResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.lock:

                GoogleClient.getInstance(this).getClient().signOut();
                Intent schedule = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(schedule);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void update(int count) {

        if(count>0){

            makeWish.setVisibility(View.VISIBLE);
        }else{

            makeWish.setVisibility(View.INVISIBLE);
        }
    }

    public static void unSelect(){
        for(int i = 0; i<employees.size(); i++){
            employees.get(i).setSelected(false);
        }
    }

    public void makeWish(View view){


        Intent intent = new Intent(HomeActivity.this, TemplateActivity.class);
        ArrayList<Integer> empIds = new ArrayList<>();
        for(Employee emp: employees){
            if(emp.getSelected()){
                empIds.add(emp.getId());
            }
        }

        intent.putIntegerArrayListExtra("empIds",empIds);
        intent.putExtra("type",type);
        intent.putExtra("date",selDate);
        startActivity(intent);

    }

    class FetchList extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            String token = GoogleSignIn.getLastSignedInAccount(getApplicationContext()).getIdToken();

            Call<BirthdayResponse> call = RetrofitClient.getInstance().getApi().getBirthdays(token);

            call.enqueue(new Callback<BirthdayResponse>() {
                @Override
                public void onResponse(Call<BirthdayResponse> call, Response<BirthdayResponse> response) {

                    if (response.code() == 200) {

                        BirthdayResponse birthdayResponse = response.body();
                        HomeActivity.employees = birthdayResponse.getEmployees();
                        HomeActivity.unSelect();

                        nothingFound.setVisibility(View.INVISIBLE);

                        showList("MakeWish",currentDate);

                    }else if(response.code() == 401){
                        Toast.makeText(getApplicationContext(),"Token Expired. Please Login Again.",Toast.LENGTH_LONG).show();
                        logout();
                    }else if(response.code() == 404){

                        nothingFound.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<BirthdayResponse> call, Throwable t) {

                }
            });

            return null;
        }
    }

    public void logout(){

       GoogleClient.getInstance(this).getClient().signOut();
        Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void showList(String type, String date){

        this.type = type;
        makeWish.setText(type);
        if(type.equalsIgnoreCase("makeWish"))
            makeWish.setText("Make A Wish");
        if(HomeActivity.employees.isEmpty()){

            adapter.notifyDataSetChanged();
            nothingFound.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);

        }else{

            nothingFound.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new MyAdapter(HomeActivity.employees, getApplicationContext(),type, buttonStatus);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
