package com.nahashon.second.activity;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nahashon.second.R;
import com.nahashon.second.adapter.Adapter;
import com.nahashon.second.adapter.Question;
import com.nahashon.second.interfaces.AskTeacherInterface;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AskTeacher extends AppCompatActivity implements AskTeacherInterface{
    ImageView sendQuestion;
    EditText newQuestion;
    ListView questionList;
    DatabaseReference mRef;
    FirebaseDatabase database;
    BottomNavigationView bottom;
    RecyclerView list1;
    List<Question> list = new ArrayList<>();
    List<Question> invert = new ArrayList<>();
    List<Question> answered = new ArrayList<>();
    List<Question> pending = new ArrayList<>();
    List<Question> myQ = new ArrayList<>();
    List<Question> archives = new ArrayList<>();
    FirebaseAuth mAuth;
    Adapter myQAdapter,pendingAdapter,answeredAdapter,archivesAdapter;


    public void answered() {


        list1.setAdapter(answeredAdapter);

    }


    public void pending() {

        list1.setAdapter(pendingAdapter);

    }


    public void archives() {

       list1.setAdapter(archivesAdapter);
    }

    public void myQ() {

        list1.setAdapter(myQAdapter);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_teacher);
        mAuth=FirebaseAuth.getInstance();



        //Firebase Instance and References.
        database = FirebaseDatabase.getInstance();

        mRef = database.getReference().child("Questions");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                invert.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    list.add(snapshot.getValue(Question.class));

                }

               for(int i=list.size()-1;i>=0;i--){
                    invert.add(list.get(i));


               }
                answered.clear();
                myQ.clear();
                pending.clear();
                archives.clear();
                for(int i=0;i<invert.size();i++){
                    if(!invert.get(i).getAnswer().equals("pending")){
                        answered.add(invert.get(i));
                    }
                    if(invert.get(i).getAnswer().equals("pending")){
                        pending.add(invert.get(i));


                    }
                    if(invert.get(i).getSender().equals(mAuth.getCurrentUser().getEmail())){
                        myQ.add(invert.get(i));


                    }




                }

                pendingAdapter.notifyDataSetChanged();
                archivesAdapter.notifyDataSetChanged();
                answeredAdapter.notifyDataSetChanged();
                archivesAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Calling the ImageView and the textView to type my question
        newQuestion = findViewById(R.id.new_question);
        sendQuestion = findViewById(R.id.send_question);
        //questionList = findViewById(R.id.question_list);
        bottom= findViewById(R.id.bottom);
        list1 = findViewById(R.id.list1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list1.setLayoutManager(layoutManager);


        sendQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sendQuiz()) {
                    Toast.makeText(getApplicationContext(), "Sent", Toast.LENGTH_LONG).show();
                    newQuestion.setText("");

                }
            }
        });

        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            public boolean onNavigationItemSelected(MenuItem menuItem){
                switch(menuItem.getItemId()){
                    case R.id.myQ:
                        myQ();

                        return true;
                    case R.id.answered:
                        answered();
                        Toast.makeText(getApplicationContext(),"answered",Toast.LENGTH_LONG).show();

                        return true;
                    case R.id.pending:
                        pending();

                        return true;
                    case R.id.archives:
                        archives();

                        return true;
                    default:

                        return false;


                }


            }




        });
        pendingAdapter = new Adapter(pending);
        myQAdapter= new Adapter(myQ);
        answeredAdapter = new Adapter(answered);
        archivesAdapter = new Adapter(archives);


    }


    public boolean sendQuiz() {
        boolean results = true;
        if (newQuestion.getText().toString().isEmpty() || newQuestion.getText().toString().length() < 10) {
            results = false;


        } else {
            String key = mRef.push().getKey();
            mRef.child(key).setValue(new Question(mAuth.getCurrentUser().getEmail(), DateFormat.getDateTimeInstance().format(new Date()), newQuestion.getText().toString().trim()));
            myQ();
        }
        return results;

    }
    public void onStart(){
        super.onStart();
        if(mAuth.getCurrentUser()==null){
            startActivity(new Intent(this, LoginActivity.class));
        }else{

            bottom.setSelectedItemId(R.id.myQ);
            myQ();
        }
    }

}
