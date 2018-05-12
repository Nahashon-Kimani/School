package com.nahashon.second.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.nahashon.second.R;

/**
 * Created by sam karu on 11/05/2018.
 */

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText userName,password;
    String email,pass;
    TextInputLayout userNameInput,passwordLayout;



    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        mAuth=FirebaseAuth.getInstance();
        userName=findViewById(R.id.email);
        password=findViewById(R.id.password);
        userNameInput=findViewById(R.id.textInputLayout2);
        passwordLayout=findViewById(R.id.textInputLayout);


    }
    public boolean validate() {
        email = userName.getText().toString();
        pass = password.getText().toString();

        if (email.isEmpty() || pass.isEmpty()) {
            if (email.isEmpty()) {
                userNameInput.setError("Email cannot be empty");
            }


                if (pass.isEmpty()) {
                    passwordLayout.setError("Password cannot be Empty");

                }

                return false;

            }
            if(email.indexOf("@")==-1){
                userNameInput.setError("Use the Correct Email");
            return false;
            }
            if(pass.length()<4){
                passwordLayout.setError("Password Length cannot be less than four");
                return false;
            }
            return true;


        }

    public void signIn(){
        mAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>(){
                    public void onComplete(Task<AuthResult> result){
                        if(result.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));


                        }else if(result.isCanceled()){
                            AlertDialog.Builder builder=new AlertDialog.Builder(getApplicationContext());
                            builder.setMessage("Login Canceled");
                            builder.setTitle("Error:");
                            builder.setIcon(android.R.drawable.ic_dialog_alert);

                        }


                    }

                });


    }
    public void logIn(View view){
        if(validate()){
            signIn();
        }else{
            Snackbar.make(view,"Failed try Again",Snackbar.LENGTH_SHORT).show();
        }



    }
    public void signUp(View view){

        startActivity(new Intent(this,RegesterActivity.class));
    }



}
