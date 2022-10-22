package com.jkutkut.guessnumbergame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // TODO horizontal orientation
    // TODO exception while changing theme

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
    private Button btnReset;
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
        btnReset = findViewById(R.id.btnReset);
        tbtnMode = findViewById(R.id.tbtnMode);
        txtvRemaining = findViewById(R.id.txtvRemaining);
        txtvInfo = findViewById(R.id.txtvInfo);
        etGuess = findViewById(R.id.etGuess);
        btnGuess = findViewById(R.id.btnGuess);

        tbtnMode.setChecked(!darkMode()); // Set tbtnMode to the current state

        // ********* UI Listeners *********
        btnReset.setOnClickListener(v -> initGame());
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
        initGame();
    }

    private void initGame() {
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
                alert(getString(R.string.guessNotInRange));
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
        String info;
        String remaining = "";
        if (!running) {
            // Update txtvInfo
            if (won) {
                if (remainingAttempts == ATTEMPTS - 1)
                    info = getString(R.string.winMsgPerfect);
                else {
                    info = String.format(
                            getString(R.string.winMsg),
                            ATTEMPTS - remainingAttempts
                    );
                }
            }
            else {
                info = String.format(
                        getString(R.string.loseMsg),
                        nbrToGuess
                );
            }

            // Update btnGuess
            btnGuess.setEnabled(false);
            btnGuess.setText(getString(R.string.btnGuessDisabled));
        }
        else {
            // Update txtvInfo
            if (lastGuess == -1) {
                info = String.format(
                        getString(R.string.subject),
                        remainingAttempts
                );
            }
            else if (lastGuess < nbrToGuess)
                info = getString(R.string.tooLow);
            else
                info = getString(R.string.tooHigh);

            // Update txtvRemaining
            // remainingAttempts == ATTEMPTS => ""
            if (remainingAttempts < ATTEMPTS) {
                remaining = (remainingAttempts == 1) ?
                        getString(R.string.guessesRemainingSingular) :
                        getString(R.string.guessesRemaining);
            }
            remaining = String.format(remaining, remainingAttempts);

            // Update btnGuess
            btnGuess.setEnabled(true);
        }
        txtvInfo.setText(info);
        txtvRemaining.setText(remaining);
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
            index = PALETTE[mode].length - 1;
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