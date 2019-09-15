package com.info.studenter;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private Toolbar toolbar;
    private RecyclerView rv;
    private FloatingActionButton fab;
    private ArrayList<students> studentsArrayList;
    private studentsAdapter adapter;
    private FirebaseDatabase database;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        rv = findViewById(R.id.rv);
        fab = findViewById(R.id.fab);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("students");


        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        toolbar.setTitle("Students");
        setSupportActionBar(toolbar);

        studentsArrayList = new ArrayList<>();



        adapter = new studentsAdapter(this, studentsArrayList,myRef);

        rv.setAdapter(adapter);


        allStudents();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertShow();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu,menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.e("onQueryTextSubmit",query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.e("onQueryTextChange",newText);
        search(newText);
        return false;
    }


    public void alertShow(){

        LayoutInflater layout = LayoutInflater.from(this);

        View design = layout.inflate(R.layout.alert_design,null);

        final EditText editTextName = design.findViewById(R.id.editTextName);
        final EditText editTextTel = design.findViewById(R.id.editTextTel);

        AlertDialog.Builder name = new AlertDialog.Builder(this);
        name.setTitle("Lägga till Student");
        name.setView(design);
        name.setPositiveButton("Lägga", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String student_name = editTextName.getText().toString().trim();
                String student_tel = editTextTel.getText().toString().trim();


//Push
                String key = myRef.push().getKey();
                students student = new students(key,student_name,student_tel);
                myRef.push().setValue(student);

                Toast.makeText(getApplicationContext(),student_name+" - "+student_tel,Toast.LENGTH_SHORT).show();
            }
        });

        name.setNegativeButton("Avbryta", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        name.create().show();
    }

    public void  allStudents(){

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            studentsArrayList.clear();

            for (DataSnapshot d:dataSnapshot.getChildren()){

             students student = d.getValue(students.class);
             student.setStudent_id(d.getKey());

             studentsArrayList.add(student);

            }
            // hämta alla studens

            adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void  search (final String searchWord){

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                studentsArrayList.clear();

                for (DataSnapshot d:dataSnapshot.getChildren()){
                    students student = d.getValue(students.class);

                     if(student.getStudent_name().contains(searchWord)){

                         student.setStudent_id(d.getKey());
                         studentsArrayList.add(student);

                     }

                }
                // hämta alla studens

                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
