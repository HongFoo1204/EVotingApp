package my.edu.utar.e_votingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginToken extends AppCompatActivity {

    private EditText etLoginToken;
    private TextView tvLoginToken;
    private Button btLoginToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_token);

        etLoginToken = findViewById(R.id.etLoginToken);
        tvLoginToken = findViewById(R.id.tvLoginToken);
        btLoginToken = findViewById(R.id.btLoginToken);

        btLoginToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Prevalent.currentOnlineUser.getToken().equals(etLoginToken.getText().toString())){
                    Toast.makeText(LoginToken.this, "The Token is correct", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginToken.this, MainActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(LoginToken.this, "The Token is Incorrect, Please enter again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}