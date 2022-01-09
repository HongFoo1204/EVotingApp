package my.edu.utar.e_votingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminVoteStatus extends AppCompatActivity {

    private TextView tvStatus;
    private Button btOpen, btClose, btBack;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_vote_status);

        tvStatus = findViewById(R.id.tvVoteStatus);
        btOpen = findViewById(R.id.btOpen);
        btClose = findViewById(R.id.btClose);
        btBack = findViewById(R.id.btVoteStatusBack);

        setStatusToTv();

        btOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVoteStatus(true);
                Toast.makeText(AdminVoteStatus.this, "Status Changed !!!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVoteStatus(false);
                Toast.makeText(AdminVoteStatus.this, "Status Changed !!!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void setStatusToTv() {
        ref = FirebaseDatabase.getInstance().getReference().child("Vote");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get option name and display
                String display;
                if ((Boolean) snapshot.child("Open").getValue()) {
                    display = "Currently Status: Open";
                }else{
                    display = "Currently Status: Closed";
                }
                tvStatus.setText(display);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setVoteStatus(Boolean status) {
        ref = FirebaseDatabase.getInstance().getReference();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ref.child("Vote").child("Open").setValue(status);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}