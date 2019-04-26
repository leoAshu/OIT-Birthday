package com.example.aparu.birthday_schedule.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.aparu.birthday_schedule.API.GoogleClient;
import com.example.aparu.birthday_schedule.API.TemplateResponse;
import com.example.aparu.birthday_schedule.Models.Template;
import com.example.aparu.birthday_schedule.R;
import com.example.aparu.birthday_schedule.API.RetrofitClient;
import com.example.aparu.birthday_schedule.Models.User;
import com.example.aparu.birthday_schedule.API.VerifyResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    static GoogleSignInClient googleSignInClient;
    static ProgressDialog dialog;
    static Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login);

        dialog=new ProgressDialog(this);
        dialog.setMessage("Logging In");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(true);

        googleSignInClient = GoogleClient.getInstance(this).getClient();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();

                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, 1);
            }
        });

    }

    public void onLoggedIn() {

        Call<TemplateResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getTemplates(GoogleSignIn.getLastSignedInAccount(this).getIdToken());

        call.enqueue(new Callback<TemplateResponse>() {
            @Override
            public void onResponse(Call<TemplateResponse> call, Response<TemplateResponse> response) {

                if(response.code() == 200){

                    TemplateResponse templateResponse = response.body();

                    TemplateActivity.templates = templateResponse.getTemplates();
                    GetTemplate task = new GetTemplate();
                    task.execute(TemplateActivity.templates);

                }
            }

            @Override
            public void onFailure(Call<TemplateResponse> call, Throwable t) {

            }
        });
        dialog.dismiss();
        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
        finish();

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("ResultCode",""+resultCode);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == 1) {

                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                final GoogleSignInAccount googleSignInAccount = task.getResult();
                String token = googleSignInAccount.getIdToken();

                Call<VerifyResponse> call = RetrofitClient.getInstance()
                        .getApi()
                        .login(token);

                call.enqueue(new Callback<VerifyResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<VerifyResponse> call, @NonNull Response<VerifyResponse> response) {
                        if (response.code() == 200) {

                            VerifyResponse verifyResponse = response.body();
                            User user = verifyResponse.getUser();

                            SharedPreferences sharedPreferences = getSharedPreferences("MyPref", MODE_PRIVATE);
                            sharedPreferences.edit().putString("User", user.toString()).apply();

                            onLoggedIn();

                        } else if (response.code() == 401) {

                            Toast.makeText(LoginActivity.this, "You are not authorized", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            googleSignInClient.signOut();
                        }
                    }

                    @Override
                    public void onFailure(Call<VerifyResponse> call, Throwable throwable) {

                        googleSignInClient.signOut();
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Verification Failed. Login Again.",Toast.LENGTH_LONG).show();
                        Log.i("Error","Verification failed! Login Again.");
                    }

                });
            }
        }else{
            Toast.makeText(LoginActivity.this,""+resultCode,Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }
    }

    class GetTemplate extends AsyncTask<ArrayList<Template>, Void, Void>{

        @Override
        protected Void doInBackground(ArrayList<Template>... arrayLists) {

            TemplateActivity.drawables.clear();
            for(int i = 0;i<arrayLists[0].size();i++){

                try {
                    InputStream is = (InputStream) new URL("http://10.100.101.127:3000/"+arrayLists[0].get(i).getTemplate()).getContent();
                    TemplateActivity.drawables.add(Drawable.createFromStream(is, "src name"));
                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            return null;
        }
    }
}