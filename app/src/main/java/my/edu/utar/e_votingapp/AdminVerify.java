package my.edu.utar.e_votingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminVerify extends AppCompatActivity {

    private ListView lvUser;
    private DatabaseReference ref;
    private List<String> title_list;
    private ArrayAdapter<String> arrayAdapter;
    private Users user;
    private ArrayList<Users> itemlist = new ArrayList<Users>();
    private Button btReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_verify);

        lvUser = findViewById(R.id.lvUser);
        btReturn = findViewById(R.id.btVerifyReturn);
        title_list = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference("Users");

        arrayAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.tvItem, title_list);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayAdapter.clear();
                title_list.clear();
                for (DataSnapshot d : snapshot.getChildren()) {
                    if (!d.getValue(Users.class).isVerified()) {
                        user = d.getValue(Users.class);
                        title_list.add(user.getIC());
                        itemlist.add(user);
                    }
                }
                lvUser.setAdapter(arrayAdapter);
                lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(AdminVerify.this, UserInfo.class);

                        Users userInfo = null;
                        for (Users m : itemlist) {
                            if (m.getIC().equals(title_list.get(position)) && !m.isVerified()) {
                                userInfo = m;
                                break;
                            }
                        }
                        ArrayList<String> userInfoList = new ArrayList<>();
                        userInfoList.add(userInfo.getName());
                        userInfoList.add(userInfo.getIC());
                        userInfoList.add(userInfo.getEmail());
                        userInfoList.add(userInfo.getPhone());
                        userInfoList.add(String.valueOf(userInfo.isVerified()));
                        userInfoList.add(userInfo.getImgUrlICFront());
                        userInfoList.add(userInfo.getImgUrlICBack());
                        userInfoList.add(userInfo.getImgUrlSelfie());

                        intent.putStringArrayListExtra("item", userInfoList);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        btReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}