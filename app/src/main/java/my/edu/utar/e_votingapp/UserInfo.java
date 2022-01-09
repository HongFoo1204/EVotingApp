package my.edu.utar.e_votingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserInfo extends AppCompatActivity {

    private ListView listView2;
    private List<String> itemFetchList;
    private ArrayAdapter<String> arrayAdapter;
    private Button btVerify;
    private DatabaseReference ref;
    private ArrayList infoList;
    private ImageView ivICFront, ivICBack, ivSelfie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        listView2 = findViewById(R.id.lvUserInfo);
        btVerify = findViewById(R.id.btVerify);

        itemFetchList = getIntent().getStringArrayListExtra("item");
        infoList = new ArrayList();
        for (int i = 0; i < 5; i++) {
            infoList.add(itemFetchList.get(i));
        }
        arrayAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.tvItem, infoList);
        listView2.setAdapter(arrayAdapter);

        ivICFront = findViewById(R.id.ivVerifyICFront);
        ivICBack = findViewById(R.id.ivVerifyICBack);
        ivSelfie = findViewById(R.id.ivVerifySelfie);

        Glide.with(this).load(itemFetchList.get(5)).into(ivICFront);
        Glide.with(this).load(itemFetchList.get(6)).into(ivICBack);
        Glide.with(this).load(itemFetchList.get(7)).into(ivSelfie);

        btVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVerify(true);
                Toast.makeText(UserInfo.this, "Voter Verify Status Changed !!!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setVerify(boolean verify) {
        ref = FirebaseDatabase.getInstance().getReference().child("Users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ref.child(itemFetchList.get(1)).child("Verified").setValue(verify);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}