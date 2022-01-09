package my.edu.utar.e_votingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Token extends AppCompatActivity {

    private EditText etToken;
    private TextView tvToken, title1, title2, title3;
    private Button btToken1, btToken2;
    private Users user;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token);

        mRef = FirebaseDatabase.getInstance().getReference();

        etToken = findViewById(R.id.etToken);
        tvToken = findViewById(R.id.tvToken);
        title1 = findViewById(R.id.tvTokenTitle1);
        title2 = findViewById(R.id.tvTokenTitle2);
        title3 = findViewById(R.id.tvTokenTitle3);
        btToken1 = findViewById(R.id.btToken1);
        btToken2 = findViewById(R.id.btToken2);

        user = (Users) getIntent().getSerializableExtra("User");

        user.setToken(getToken());
        tvToken.setText(user.getToken());
        btToken1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Token.this);
                builder1.setMessage("Are you sure you already memories or recorded down the Token?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Invisible first page
                                tvToken.setVisibility(View.INVISIBLE);
                                title1.setVisibility(View.INVISIBLE);
                                title2.setVisibility(View.INVISIBLE);
                                btToken1.setVisibility(View.INVISIBLE);
                                //Visible second page
                                etToken.setVisibility(View.VISIBLE);
                                title3.setVisibility(View.VISIBLE);
                                btToken2.setVisibility(View.VISIBLE);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder1.create();
                alert.show();
            }
        });

        btToken2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etToken.getText().toString().equals(tvToken.getText().toString())) {
                    CreateAccount();
                    Intent i = new Intent();

                } else {
                    //Invisible first page
                    etToken.setVisibility(View.INVISIBLE);
                    title3.setVisibility(View.INVISIBLE);
                    btToken2.setVisibility(View.INVISIBLE);

                    //Visible second page
                    tvToken.setVisibility(View.VISIBLE);
                    title1.setVisibility(View.VISIBLE);
                    title2.setVisibility(View.VISIBLE);
                    btToken1.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public static String getToken() {
        final String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i <= 20; i++) {
            Random rand = new Random();
            result.append(characters.charAt(rand.nextInt(characters.length())));
        }
        return result.toString();
    }

    private void CreateAccount() {
        Map<String, Object> details = new HashMap<String, Object>();
        details.put("Name", user.getName());
        details.put("IC", user.getIC());
        details.put("Email", user.getEmail());
        details.put("Phone", user.getPhone());
        details.put("Password", user.getPassword());
        details.put("Voted", user.isVoted());
        details.put("Verified", user.isVerified());
        details.put("Token", user.getToken());
        details.put("imgUrlICFront", user.getImgUrlICFront());
        details.put("imgUrlICBack", user.getImgUrlICBack());
        details.put("imgUrlSelfie", user.getImgUrlSelfie());
        mRef.child("Users").child(user.getIC()).updateChildren(details)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Token.this, "Your register is completed!", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(Token.this, Login.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.putExtra("User", user);
                            startActivity(i);
                        } else {
                            Toast.makeText(Token.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}