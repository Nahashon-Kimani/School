package com.nahashon.second.activity;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.nahashon.second.R;

public class RegesterActivity extends AppCompatActivity {
    FirebaseAuth mauth;
    String emailString1,emailString2,passwordString1,passwordString2,verifiedemail,verifiedPassword;
    TextInputLayout email1,email2,password1,password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regester_account_activity);
        email1=findViewById(R.id.email1);
        email2=findViewById(R.id.email2);
        password1=findViewById(R.id.password1);
        password2=findViewById(R.id.password2);
        mauth = FirebaseAuth.getInstance();
    }


    public boolean validate(){
        boolean valid = true;
        emailString1=email1.getEditText().getText().toString().trim();
        emailString2=email2.getEditText().getText().toString().trim();
        passwordString1=password1.getEditText().getText().toString().trim();
        passwordString2=password2.getEditText().getText().toString().trim();

        if(!emailString1.equals(emailString2)){
            email2.setError("Both Emails must be the same");
             valid=  false;
        }
        if(!passwordString1.equals(passwordString2)){
            password2.setError("Both Emails must be the same");
            valid =  false;
        }
        if(passwordString1.length()<4){
            password1.setError("Weak password try more than 4 Characters");
            valid = false;

        }
        if(emailString1.indexOf("@")==-1){

            email1.setError("Not a valid email");
            valid = false;
        }



        return valid;
    }
    public void signUp(){
        mauth.createUserWithEmailAndPassword(verifiedemail,verifiedPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if(task.isSuccessful()){
                           if(mauth.getCurrentUser()!=null){
                               mauth.signOut();
                               startActivity(new Intent(getApplicationContext(), LoginActivity.class));


                           }


                        }

                    }
                });

    }
    public void join(View view){
        if(validate()){
            verifiedemail=emailString1;
            verifiedPassword=passwordString1;
            signUp();
        }

    }
}
