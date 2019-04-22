package com.example.aparu.birthday_schedule.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.aparu.birthday_schedule.API.GoogleClient;
import com.example.aparu.birthday_schedule.API.RetrofitClient;
import com.example.aparu.birthday_schedule.API.TemplateResponse;
import com.example.aparu.birthday_schedule.Models.Template;
import com.example.aparu.birthday_schedule.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                verify();
            }
        },3000);
    }

    protected void verify(){
        final GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);

        if(googleSignInAccount != null)
        {
            Call<ResponseBody> call = RetrofitClient.getInstance()
                    .getApi()
                    .verify(GoogleSignIn.getLastSignedInAccount(getApplicationContext()).getIdToken());

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.code()!=200){

                        Toast.makeText(getApplicationContext(),"Token Expired. Please Login Again.",Toast.LENGTH_LONG).show();
                        GoogleClient.getInstance(MainActivity.this).getClient().signOut();
                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();

                    }else if(response.code()==200){

                        onLoggedIn();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }else{

            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    class GetTemplate extends AsyncTask<ArrayList<Template>, Void, Void> {

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
                    MainActivity.GetTemplate task = new MainActivity.GetTemplate();
                    task.execute(TemplateActivity.templates);

                }
            }

            @Override
            public void onFailure(Call<TemplateResponse> call, Throwable t) {

            }
        });
        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
        finish();

    }
}
