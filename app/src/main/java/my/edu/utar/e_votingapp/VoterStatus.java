package my.edu.utar.e_votingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class VoterStatus extends AppCompatActivity {

    private TextView tvStatus;
    private Button btLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voter_status);

        tvStatus = findViewById(R.id.tvStatus);
        btLogout = findViewById(R.id.btStatusLogout);

        if(Prevalent.currentOnlineUser.isVerified()){
            tvStatus.setText("You are eligible to vote!\nPlease login to vote when vote open!");
        }else{
            tvStatus.setText("You are not eligible to vote yet!\nPlease check again later!");
        }

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VoterStatus.this, Login.class);
                startActivity(i);
                finish();
            }
        });
    }
}