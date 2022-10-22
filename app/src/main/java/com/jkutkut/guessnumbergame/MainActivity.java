package com.jkutkut.guessnumbergame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int ATTEMPTS = 5;

    private int remainingAttempts;
    private int nbrToGuess;

    // ********* UI Elements *********
    private TextView txtvRemaining;
    private TextView txtvInfo;
    private EditText etGuess;
    private Button btnGuess;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ********* UI Elements *********
        txtvRemaining = findViewById(R.id.txtvRemaining);
        txtvInfo = findViewById(R.id.txtvInfo);
        etGuess = findViewById(R.id.etGuess);
        btnGuess = findViewById(R.id.btnGuess);

        btnGuess.setOnClickListener(v -> {
            makeGuess();
        });

        // ********* Game *********
        remainingAttempts = ATTEMPTS;
        nbrToGuess = (int) ((Math.random() * 100) + 1);
        txtvInfo.setText(
            String.format(
                getString(R.string.subject),
                remainingAttempts
            )
        );
        updateRemaining();
    }

    private void makeGuess() {
        String guessStr = etGuess.getText().toString();
        try {
            int guess = Integer.parseInt(guessStr);
            if (guess < 1 || guess > 100) {
                alert("The number must be between 1 and 100"); // TODO
                return;
            }

            remainingAttempts--;
            if (guess == nbrToGuess) {
                txtvInfo.setText("You win!"); // TODO
            }
            else if (guess < nbrToGuess) {
                txtvInfo.setText("The number is greater"); // TODO
            }
            else {
                txtvInfo.setText("The number is smaller"); // TODO
            }
            updateRemaining();
        }
        catch (NumberFormatException e) {
            alert("Please enter a number between 1 and 100"); // TODO refactor into string.xml
        }
    }

    private void alert(String msg) {
        Toast.makeText(
            this,
            msg,
            Toast.LENGTH_LONG
        ).show();
    }

    private void updateRemaining() {
        txtvRemaining.setText(
            String.format(
                getString(R.string.guessesRemaining),
                remainingAttempts
            )
        );
    }
}