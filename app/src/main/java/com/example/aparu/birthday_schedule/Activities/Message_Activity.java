package com.example.aparu.birthday_schedule.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aparu.birthday_schedule.API.EmailResponse;
import com.example.aparu.birthday_schedule.API.RetrofitClient;
import com.example.aparu.birthday_schedule.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;

import java.io.File;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Message_Activity extends AppCompatActivity {

    ImageView back_switch;
    Button send_wishes;
    ImageView template;
    TextView title;

    String type;
    int tempId;
    ArrayList<Integer> empIds = new ArrayList<>();
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_);

        back_switch = findViewById(R.id.back_switch);
        send_wishes = findViewById(R.id.send_wishes);
        title = findViewById(R.id.title);

        type = getIntent().getStringExtra("type");
        tempId = TemplateActivity.templates.get(getIntent().getIntExtra("temp", 0)).getId();
        empIds.add(getIntent().getIntExtra("id", 0));
        Log.i("Emp_id", "" + getIntent().getIntExtra("id", 0));
        if (!type.equalsIgnoreCase("edit"))
            date = getIntent().getStringExtra("date");
        //Log.i("date",date);

        if (type.equalsIgnoreCase("schedule")) {

            send_wishes.setText("Schedule");

        } else if (type.equalsIgnoreCase("edit")) {

            title.setText("Edit");
            send_wishes.setText("Save");
        } else {

            send_wishes.setText("Send");
        }

        send_wishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equalsIgnoreCase("edit")){

                    Log.i("Save","ScheduleId: "+getIntent().getIntExtra("id",0)+" Type: "+getIntent().getStringExtra("type"));
                    String token = GoogleSignIn.getLastSignedInAccount(getApplicationContext()).getIdToken();
                    int scheduleId = getIntent().getIntExtra("id",0);
                    Call<ResponseBody> call = RetrofitClient.getInstance()
                            .getApi()
                            .update(token, scheduleId, tempId);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.code()==200){

                                Toast.makeText(getApplicationContext(),"Updated Successfully",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(),Scheduled_Wishes.class);
                                startActivity(intent);
                                finish();

                            }else if(response.code()==401){

                                Toast.makeText(getApplicationContext(),"Token Expired. Please Login Again.",Toast.LENGTH_LONG).show();
                                logout();
                            }else{

                                Toast.makeText(getApplicationContext(),"Update Failed",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(),Scheduled_Wishes.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable throwable) {

                        }
                    });

                }else{
                    sendEmail();
                }
            }
        });

        back_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(type.equalsIgnoreCase("edit")){

                    Intent select_template = new Intent(getApplicationContext(), Scheduled_Wishes.class);
                    startActivity(select_template);
                    finish();

                }else{

                    Intent select_template = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(select_template);
                    finish();
                }
            }
        });

        template = findViewById(R.id.template);

        int p = getIntent().getIntExtra("temp", 0);

        template.setImageDrawable(TemplateActivity.drawables.get(p));


    }

    public void sendEmail() {
        if (type.equalsIgnoreCase("Schedule")) {

            Call<EmailResponse> call = RetrofitClient.getInstance()
                    .getApi()
                    .schedule(GoogleSignIn.getLastSignedInAccount(this).getIdToken(), tempId, empIds, date, "");
            //Log.i("date",date);
            call.enqueue(new Callback<EmailResponse>() {
                @Override
                public void onResponse(Call<EmailResponse> call, Response<EmailResponse> response) {

                    if (response.code() == 200) {
                        EmailResponse emailResponse = response.body();
                        Toast.makeText(Message_Activity.this, emailResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    } else if (response.code() == 401) {
                        Toast.makeText(getApplicationContext(), "Token Expired. Please Login Again.", Toast.LENGTH_LONG).show();
                        logout();
                    } else {

                        EmailResponse emailResponse = response.body();
                        Toast.makeText(Message_Activity.this, emailResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<EmailResponse> call, Throwable t) {

                    Toast.makeText(getApplicationContext(), "Response Fail", Toast.LENGTH_SHORT).show();
                }
            });

            startActivity(new Intent(Message_Activity.this, HomeActivity.class));
            finish();

        } else {

            Call<EmailResponse> call = RetrofitClient.getInstance()
                    .getApi()
                    .send(GoogleSignIn.getLastSignedInAccount(this).getIdToken(), tempId, empIds);

            call.enqueue(new Callback<EmailResponse>() {
                @Override
                public void onResponse(Call<EmailResponse> call, Response<EmailResponse> response) {

                    if (response.code() == 200) {
                        EmailResponse emailResponse = response.body();
                        Toast.makeText(Message_Activity.this, emailResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Message_Activity.this, HomeActivity.class));
                    }
                }

                @Override
                public void onFailure(Call<EmailResponse> call, Throwable t) {

                }
            });

        }
    }

    public void logout() {

        LoginActivity.googleSignInClient.signOut();
        Intent intent = new Intent(Message_Activity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}