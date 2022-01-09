package my.edu.utar.e_votingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Register extends AppCompatActivity {

    EditText mName, mIC, mEmail, mPhone, mPassword, mPassword2, mCountryCode;
    Button mRegisterBtn;
    TextView mLoginHereBtn;
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mName = findViewById(R.id.etRegName);
        mIC = findViewById(R.id.etRegIC);
        mEmail = findViewById(R.id.etRegEmail);
        mPhone = findViewById(R.id.etRegPhone);
        mPassword = findViewById(R.id.etRegPassword);
        mPassword2 = findViewById(R.id.etRegPassword2);
        mRegisterBtn = findViewById(R.id.btRegister);
        mLoginHereBtn = findViewById(R.id.tvLoginHere);
        mCountryCode = findViewById(R.id.etCountryCode);

        mRef = FirebaseDatabase.getInstance().getReference();

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mName.getText().toString())) {
                    Toast.makeText(Register.this, "Please enter your complete your information!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mIC.getText().toString())) {
                    Toast.makeText(Register.this, "Please enter your complete your information!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mEmail.getText().toString())) {
                    Toast.makeText(Register.this, "Please enter your complete your information!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mPhone.getText().toString())) {
                    Toast.makeText(Register.this, "Please enter your complete your information!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mPassword.getText().toString())) {
                    Toast.makeText(Register.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mPassword2.getText().toString())) {
                    Toast.makeText(Register.this, "Please confirm you password", Toast.LENGTH_SHORT).show();
                } else if (!(mPassword.getText().toString().equals(mPassword2.getText().toString()))) {
                    Toast.makeText(Register.this, "Please confirm your second password is same with first password", Toast.LENGTH_SHORT).show();
                } else {
                    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (!(dataSnapshot.child("Users").child(mIC.getText().toString()).exists())) {
                                Users user = new Users();
                                user.setName(mName.getText().toString());
                                user.setIC(mIC.getText().toString());
                                user.setEmail(mEmail.getText().toString());
                                user.setPhone(mPhone.getText().toString());
                                user.setPassword(mPassword.getText().toString());
                                user.setVoted(false);
                                user.setVerified(false);
                                Intent i = new Intent(Register.this, PhoneAuth.class);
                                i.putExtra("User", user);
                                i.putExtra("CountryCode", mCountryCode.getText().toString());
                                startActivity(i);
                            } else {
                                Toast.makeText(Register.this, "Account with this number already exist..", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        mLoginHereBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }
}
