package my.edu.utar.e_votingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneAuth extends AppCompatActivity {

    private EditText optInput;
    private Button submit;
    private ProgressDialog loadingBar;
    private String countryCode, phone, fullPhone, verificationId;
    private Users user;
    private TextView displayPhone;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        user = (Users) getIntent().getSerializableExtra("User");
        countryCode = getIntent().getStringExtra("CountryCode");
        phone = user.getPhone();
        fullPhone = countryCode + phone;

        optInput = findViewById(R.id.etOtp);
        submit = findViewById(R.id.btOtp);
        displayPhone = findViewById(R.id.tvPhoneNum);

        loadingBar = new ProgressDialog(this);

        displayPhone.setText(fullPhone);
        sendVerificationCode(fullPhone);    //pass with country code

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = optInput.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    optInput.setError("Enter Code..");
                    optInput.requestFocus();
                    return;
                }
                loadingBar.setTitle("Please Wait..");
                loadingBar.setMessage("Please Wait while we are checking our credentials...");
                loadingBar.show();
                verifyCode(code);
            }
        });
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        loadingBar.dismiss();
                        user.setPhone(fullPhone);
                        Intent i = new Intent(PhoneAuth.this, ICImageUpload.class);
                        i.putExtra("User", user);
                        startActivity(i);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingBar.dismiss();
                        Toast.makeText(PhoneAuth.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void sendVerificationCode(String Phone) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(Phone)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                loadingBar.setTitle("Please Wait..");
                loadingBar.setMessage("Please Wait while we are checking our credentials...");
                loadingBar.show();
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            loadingBar.dismiss();
            Toast.makeText(PhoneAuth.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };
}