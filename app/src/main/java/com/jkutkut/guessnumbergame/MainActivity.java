package com.jkutkut.guessnumbergame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int ATTEMPTS = 5;

    private boolean running;
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
        running = true;
        remainingAttempts = ATTEMPTS;
        nbrToGuess = (int) ((Math.random() * 100) + 1);
        System.out.println("Number to guess: " + nbrToGuess);
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

            if (guess == nbrToGuess) {
                running = false;
            }
            else if (guess < nbrToGuess) {
                txtvInfo.setText(getString(R.string.tooLow));
            }
            else {
                txtvInfo.setText(getString(R.string.tooGreat));
            }
            if (--remainingAttempts == 0) {
                running = false;
            }
            updateRemaining();
        }
        catch (NumberFormatException e) {
            alert(getString(R.string.invalidNumberException));
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
        if (!running) {
            txtvRemaining.setText("");
            txtvInfo.setText(
                String.format(
                    getString(
                        (remainingAttempts == 0) ? R.string.looseMsg : R.string.winMsg
                    ),
                    nbrToGuess,
                    ATTEMPTS - remainingAttempts // TODO singular msg
                )
            );
            btnGuess.setEnabled(false);
            btnGuess.setText("Game ended");
        }
        else {
            txtvRemaining.setText(
                String.format(
                    (remainingAttempts == ATTEMPTS) ? "" : getString(R.string.guessesRemaining),
                    remainingAttempts
                )
            );
        }
    }
}