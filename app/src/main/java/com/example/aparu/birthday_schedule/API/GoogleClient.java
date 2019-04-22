package com.example.aparu.birthday_schedule.API;

import android.content.Context;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class GoogleClient {

    private GoogleSignInClient googleSignInClient;
    private static GoogleClient mInstance;

    private GoogleClient(Context context){

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("388935096174-6iqg1fq3gs3h1ecs8u0m66iiku1gvtil.apps.googleusercontent.com")
                .requestServerAuthCode("388935096174-6iqg1fq3gs3h1ecs8u0m66iiku1gvtil.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(context,gso);
    }
    public static synchronized GoogleClient getInstance(Context context){

        if(mInstance == null){
            mInstance = new GoogleClient(context);
        }
        return mInstance;
    }
    public GoogleSignInClient getClient(){
        return googleSignInClient;
    }
}
