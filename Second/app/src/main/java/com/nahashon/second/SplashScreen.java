package com.nahashon.second;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.nahashon.second.R;
import com.nahashon.second.activity.LoginActivity;
import com.nahashon.second.activity.MainActivity;

import java.io.IOException;

public class SplashScreen extends AppCompatActivity {
    FirebaseAuth mAuth;
    ImageView  imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mAuth=FirebaseAuth.getInstance();
        imageView = findViewById(R.id.logo);
        new Thread(new Runnable(){
            public void run(){
                try {
                    Thread.sleep(3000);
                    imageView.post(new Runnable(){
                        public void run(){
                         userCheck();
                        }
                    });
                }
                catch (InterruptedException e){

                }


            }

        }).start();

    }
    public void userCheck(){

        if(mAuth.getCurrentUser()==null){
            Intent a = new Intent(getApplicationContext(), LoginActivity.class);
            a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);
        }else{
            Intent b = new Intent(getApplicationContext(), MainActivity.class);
            b.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(b);
        }
    }
}
