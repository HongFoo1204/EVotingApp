package my.edu.utar.e_votingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminEdit extends AppCompatActivity {

    private EditText etOption1, etOption2;
    private Button btSubmit;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit);

        etOption1 = findViewById(R.id.etEditOption1);
        etOption2 = findViewById(R.id.etEditOption2);
        btSubmit = findViewById(R.id.btEditSubmit);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeOptionName("Option1", etOption1.getText().toString());
                changeOptionName("Option2", etOption2.getText().toString());

                Toast.makeText(AdminEdit.this, "Edit Successful !!!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), AdminActivity.class));
            }
        });
    }

    private void changeOptionName(String option, String optionName) {
        ref = FirebaseDatabase.getInstance().getReference();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ref.child("Vote").child(option).child("Name").setValue(optionName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}