package my.edu.utar.e_votingapp;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Users implements Serializable {
    private String Name, IC, Email, Phone, Password, Token, imgUrlICFront, imgUrlICBack, imgUrlSelfie;
    private boolean Voted, Verified;
}
