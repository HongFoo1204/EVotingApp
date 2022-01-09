package my.edu.utar.e_votingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminResult extends AppCompatActivity {

    private TextView tvTotal, tvOption1, tvOption2,tvAmount1,tvAmount2;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_result);

        tvTotal = findViewById(R.id.tvResultTotal);
        tvOption1 = findViewById(R.id.tvResult1);
        tvOption2 = findViewById(R.id.tvResult2);
        tvAmount1 = findViewById(R.id.tvResultAmount1);
        tvAmount2 = findViewById(R.id.tvResultAmount2);

        //get data from database
        ref = FirebaseDatabase.getInstance().getReference().child("Vote");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get option name and display
                String result1 = snapshot.child("Option1").child("Name").getValue().toString();
                String result2 = snapshot.child("Option2").child("Name").getValue().toString();
                result1 += ": ";
                result2 += ": ";
                tvOption1.setText(result1);
                tvOption2.setText(result2);
                //get vote number and display
                int amount1 = Integer.parseInt(snapshot.child("Option1").child("Amount").getValue().toString());
                int amount2 = Integer.parseInt(snapshot.child("Option2").child("Amount").getValue().toString());
                int total = amount1 + amount2;
                tvTotal.setText(String.valueOf(total));
                tvAmount1.setText(String.valueOf(amount1));
                tvAmount2.setText(String.valueOf(amount2));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}