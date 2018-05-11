package com.nahashon.second.activity;

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


    public void answered() {
        answered.clear();
        for(int i=0;i<invert.size();i++){
            if(!invert.get(i).getAnswer().equals("pending")){
                answered.add(invert.get(i));


            }



        }

        list1.setAdapter(new Adapter(answered));

    }


    public void pending() {
        pending.clear();
        for(int i=0;i<list.size();i++){
            if(invert.get(i).getAnswer().equals("Pending")){
                pending.add(invert.get(i));


            }



        }

        list1.setAdapter(new Adapter(pending));

    }


    public void archives() {


    }

    public void myQ() {
        myQ.clear();
        for(int i=0;i<list.size();i++){
            if(!invert.get(i).getAnswer().equals("pending")){
                myQ.add(invert.get(i));


            }



        }
        list1.setAdapter(new Adapter(myQ));


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_teacher);



        //Firebase Instance and References.
        database = FirebaseDatabase.getInstance();

        mRef = database.getReference().child("Questions");
        mRef.keepSynced(true);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    list.add(snapshot.getValue(Question.class));

                }

               for(int i=list.size()-1;i>=0;i--){
                    invert.add(list.get(i));


               }
               pending();

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


    }


    public boolean sendQuiz() {
        boolean results = true;
        if (newQuestion.getText().toString().isEmpty() || newQuestion.getText().toString().length() < 10) {
            results = false;


        } else {
            String key = mRef.push().getKey();
            mRef.child(key).setValue(new Question("Sam", DateFormat.getDateTimeInstance().format(new Date()), newQuestion.getText().toString().trim()));
        }
        return results;

    }

}
