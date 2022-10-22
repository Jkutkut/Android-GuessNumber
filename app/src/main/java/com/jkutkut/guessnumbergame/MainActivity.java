package com.jkutkut.guessnumbergame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    // TODO horizontal orientation

    private static final int ATTEMPTS = 5;

    private static final int[][] PALETTE = {
        { // Light mode
            R.color.bg_5_light,
            R.color.bg_4_light,
            R.color.bg_3_light,
            R.color.bg_2_light,
            R.color.bg_1_light,
            R.color.bg_invalid_light,
            R.color.bg_valid_light
        },
        { // Dark mode
            R.color.bg_5_dark,
            R.color.bg_4_dark,
            R.color.bg_3_dark,
            R.color.bg_2_dark,
            R.color.bg_1_dark,
            R.color.bg_invalid_dark,
            R.color.bg_valid_dark
        }
    };

    private boolean running;
    private boolean won;
    private int remainingAttempts;
    private int nbrToGuess;
    private int lastGuess;

    // ********* UI Elements *********
    private RelativeLayout menu;
    private ToggleButton tbtnMode;
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
        menu = findViewById(R.id.rlMenu);
        tbtnMode = findViewById(R.id.tbtnMode);
        txtvRemaining = findViewById(R.id.txtvRemaining);
        txtvInfo = findViewById(R.id.txtvInfo);
        etGuess = findViewById(R.id.etGuess);
        btnGuess = findViewById(R.id.btnGuess);

        tbtnMode.setChecked(!darkMode()); // Set tbtnMode to the current state

        // ********* UI Listeners *********
        tbtnMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        });
        btnGuess.setOnClickListener(v -> {
            makeGuess();
        });

        // ********* Game *********
        lastGuess = -1;
        running = true;
        won = false;
        remainingAttempts = ATTEMPTS;
        nbrToGuess = (int) ((Math.random() * 100) + 1);
        System.out.println("Number to guess: " + nbrToGuess);
        updateUI();
    }

    private void makeGuess() {
        String guessStr = etGuess.getText().toString();
        try {
            int guess = Integer.parseInt(guessStr);
            if (guess < 1 || guess > 100) {
                alert("The number must be between 1 and 100"); // TODO
                return;
            }
            lastGuess = guess;
            if (guess == nbrToGuess) {
                running = false;
                won = true;
            }
            if (--remainingAttempts == 0) {
                running = false;
            }
            updateUI();
        }
        catch (NumberFormatException e) {
            alert(getString(R.string.invalidNumberException));
        }
    }

    private void updateUI() {
        if (!running) {
            txtvRemaining.setText("");
            txtvInfo.setText(
                String.format(
                    getString(
                        (won) ? R.string.winMsg : R.string.looseMsg
                    ),
                    nbrToGuess,
                    ATTEMPTS - remainingAttempts // TODO singular msg
                )
            );
            btnGuess.setEnabled(false);
            btnGuess.setText("Game ended");
        }
        else {
            if (lastGuess == -1)
                txtvInfo.setText(
                    String.format(
                        getString(R.string.subject),
                        remainingAttempts
                    )
                );
            else if (lastGuess < nbrToGuess)
                txtvInfo.setText(getString(R.string.tooLow));
            else
                txtvInfo.setText(getString(R.string.tooGreat));
            txtvRemaining.setText(
                String.format(
                    (remainingAttempts == ATTEMPTS) ? "" : getString(R.string.guessesRemaining),
                    remainingAttempts
                )
            ); // TODO singular msg
        }
        menu.setBackgroundColor(getBgColor());
    }

    // ********* Tools *********
    private void alert(String msg) {
        Toast.makeText(
                this,
                msg,
                Toast.LENGTH_LONG
        ).show();
    }

    private int getBgColor() {
        int mode = (darkMode())? 1 : 0;
        int index = ATTEMPTS - remainingAttempts;
        if (!running && won)
            index = ATTEMPTS;
        return getColor(PALETTE[mode][index]);
    }

    private boolean darkMode() {
        int nightModeFlags = this.getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }

    // ********* Session Restoration *********
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("running", running);
        outState.putBoolean("won", won);
        outState.putInt("remainingAttempts", remainingAttempts);
        outState.putInt("nbrToGuess", nbrToGuess);
        outState.putInt("lastGuess", lastGuess);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        running = savedInstanceState.getBoolean("running");
        won = savedInstanceState.getBoolean("won");
        remainingAttempts = savedInstanceState.getInt("remainingAttempts");
        nbrToGuess = savedInstanceState.getInt("nbrToGuess");
        lastGuess = savedInstanceState.getInt("lastGuess");
        updateUI();
    }
}