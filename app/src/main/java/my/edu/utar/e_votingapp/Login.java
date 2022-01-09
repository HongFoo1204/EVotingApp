package my.edu.utar.e_votingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class Login extends AppCompatActivity {
    private EditText mIC, mPassword;
    private Button mLoginBtn;
    private TextView mRegisterHereBtn;
    private ProgressDialog loadingBar;
    private DatabaseReference mRef;
    private String IC, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mIC = findViewById(R.id.etLogPhone);
        mPassword = findViewById(R.id.etLogPassword);
        mLoginBtn = findViewById(R.id.btLogin);
        mRegisterHereBtn = findViewById(R.id.tvRegisterHere);

        loadingBar = new ProgressDialog(this);
        mRef = FirebaseDatabase.getInstance().getReference();

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mIC.getText().toString())) {
                    Toast.makeText(Login.this, "Please enter your phone number..", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mPassword.getText().toString())) {
                    Toast.makeText(Login.this, "Please enter your password...", Toast.LENGTH_SHORT).show();
                } else {
                    IC = mIC.getText().toString();
                    password = mPassword.getText().toString();
                    //Admin login
                    if (IC.equals("0000") && password.equals("0000"))
                        AdminLogin();
                    else
                        LoginUser();
                }
            }
        });

        mRegisterHereBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });
    }

    private void AdminLogin() {
        loadingBar.setTitle("Login Admin Account");
        loadingBar.setMessage("Login to Admin Account");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        startActivity(new Intent(getApplicationContext(), AdminActivity.class));
        loadingBar.dismiss();
    }

    private void LoginUser() {
        loadingBar.setTitle("Login Account");
        loadingBar.setMessage("Please wait while we are checking our credentials..");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        AllowAccessToUser(IC, password);
    }

    private void AllowAccessToUser(final String IC, final String Password) {
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Users").child(IC).exists()) {
                    final Users userdata = snapshot.child("Users").child(IC).getValue(Users.class);
                    final Boolean Open = (Boolean) snapshot.child("Vote").child("Open").getValue();
                    if (userdata.getIC().equals(IC)) {
                        if (userdata.getPassword().equals(Password)) {
                            loadingBar.dismiss();
                            Prevalent.currentOnlineUser = userdata;
                            if (!Open) {
                                Toast.makeText(Login.this, "Logged in Successfully! Vote haven't open! Check Status!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Login.this, VoterStatus.class));
                            } else {
                                if(userdata.isVerified()) {
                                    if (userdata.isVoted()) {
                                        Toast.makeText(Login.this, "You have Voted before!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Login.this, "Logged in Successfully..", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Login.this, LoginToken.class));
                                    }
                                }else{
                                    Toast.makeText(Login.this, "You are not eligible to vote!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(Login.this, "please enter correct password..", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    loadingBar.dismiss();
                    Toast.makeText(Login.this, "please create your account first with this number ..", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}