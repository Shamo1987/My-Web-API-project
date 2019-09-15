package com.info.studenter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class studentsAdapter extends RecyclerView.Adapter<studentsAdapter.CardDesignHolder> {
    private Context mContext;
    private List<students> studentsListe;
    private DatabaseReference myRef;


    public studentsAdapter(Context mContext, List<students> studentsListe, DatabaseReference myRef) {
        this.mContext = mContext;
        this.studentsListe = studentsListe;
        this.myRef = myRef;
    }

    public class CardDesignHolder extends RecyclerView.ViewHolder{

        private TextView textViewStudentInfo;
        private ImageView ımageViewNokta;

        public CardDesignHolder(View itemView) {
            super(itemView);

            textViewStudentInfo = itemView.findViewById(R.id.textViewStudentInfo);
            ımageViewNokta = itemView.findViewById(R.id.imageViewNokta);
        }
    }


    @Override
    public CardDesignHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_card_design,parent,false);
        return new CardDesignHolder(view);
    }

    @Override
    public void onBindViewHolder(final CardDesignHolder holder, int position) {
        final students mystudent = studentsListe.get(position);

        holder.textViewStudentInfo.setText(mystudent.getStudent_name()+" - "+mystudent.getStudent_tel());

        holder.ımageViewNokta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenu = new PopupMenu(mContext,holder.ımageViewNokta);
                popupMenu.getMenuInflater().inflate(R.menu.pop_up_menu,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()){
                            case R.id.action_remove:
                                Snackbar.make(holder.ımageViewNokta,"Vill du radera?",Snackbar.LENGTH_SHORT)
                                        .setAction("Jag", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                myRef.child(mystudent.getStudent_id()).removeValue();
                                            }
                                        })
                                        .show();
                                return  true;
                            case R.id.action_update:
                                alertShow(mystudent);
                                return  true;
                                default:
                                    return false;
                        }



                    }
                });

                popupMenu.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return studentsListe.size();
    }


    public void alertShow(final students mystudent){

        LayoutInflater layout = LayoutInflater.from(mContext);

        View design = layout.inflate(R.layout.alert_design,null);

        final EditText editTextName = design.findViewById(R.id.editTextName);
        final EditText editTextTel = design.findViewById(R.id.editTextTel);

        editTextName.setText(mystudent.getStudent_name());
        editTextTel.setText(mystudent.getStudent_tel());

        AlertDialog.Builder ad = new AlertDialog.Builder(mContext);
        ad.setTitle("Student Updatera");
        ad.setView(design);
        ad.setPositiveButton("Updatera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String student_name = editTextName.getText().toString().trim();
                String student_tel = editTextTel.getText().toString().trim();



                Map<String,Object> information = new HashMap<>();
                information.put("student_name",student_name);
                information.put("student_tel",student_tel);

                myRef.child(mystudent.getStudent_id()).updateChildren(information);


                Toast.makeText(mContext,student_name+" - "+student_tel,Toast.LENGTH_SHORT).show();
            }
        });

        ad.setNegativeButton("Avbryta", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        ad.create().show();
    }


}

