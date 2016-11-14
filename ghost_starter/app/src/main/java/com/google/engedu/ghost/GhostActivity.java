package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private TextView text, label;
    private Button challenge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            // dictionary = new SimpleDictionary(inputStream);
            dictionary = new FastDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        onStart(null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("Text", text.getText().toString());
        outState.putString("Label", label.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        /*** Save the user's current game state **/
        text.setText(savedInstanceState.getString("Text"));
        label.setText(savedInstanceState.getString("Label"));

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        String keyPressed = String.valueOf(Character.toString((char) event.getUnicodeChar()));
        if (!keyPressed.matches("[a-zA-Z]+")) {
            return super.onKeyUp(keyCode, event);
        } else {
            text.append(keyPressed);
            label.setText(COMPUTER_TURN);
            computerTurn(null);
            return true;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     *
     * @param view
     * @return true
     */

    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn(view);
        }
        return true;
    }

    private void computerTurn(View view) {
        String prefix = text.getText().toString();
        if (prefix.length() >= 4)
            label.setText("Winner!");
        //String result = dictionary.getGoodWordStartingWith(prefix);
        String result = dictionary.getGoodWordStartingWith(prefix);
        if (result.length() == prefix.length()) {
            label.setText("Computer Challenegd, You Win!");
        } else if (result.isEmpty()) {
            if (dictionary.isWord(prefix))
                label.setText("Computer Challenegd, You Win!");
            else
                label.setText("Computer Challenegd, You Lose!");
        } else {
            text.setText(result.substring(0, prefix.length() + 1));
            userTurn = true;
            label.setText(USER_TURN);
        }
    }

    public void challenge(View view) {
        // String result = dictionary.getGoodWordStartingWith(text.getText().toString());
        String result = dictionary.getGoodWordStartingWith(text.getText().toString());
        if (text.getText().length() >= 4 && dictionary.isWord(text.getText().toString())) {
            label.setText("You Challenged, You Win!");
        } else if (result != "") {
            label.setText("You Challenged, Computer Win!");
            text.setText(result);
        }
    }
}
