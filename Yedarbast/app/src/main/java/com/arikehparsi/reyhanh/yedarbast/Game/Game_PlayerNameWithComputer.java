package com.arikehparsi.reyhanh.yedarbast.Game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.arikehparsi.reyhanh.yedarbast.R;

public class Game_PlayerNameWithComputer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game__player_name_with_computer);
    }

    public void submitName(View view) {
        EditText player1NameBox = (EditText) findViewById(R.id.player1);

        EditText numberOfGames = (EditText) findViewById(R.id.numberOfG);
        String player1NameText = player1NameBox.getText().toString();

        String number = numberOfGames.getText().toString();

        if(number.length()>3){
            number="999";
            numberOfGames.setText(R.string.default_max_games);
        }
        if(number.equals("") || Integer.parseInt(number)<=0){
            numberOfGames.setText(R.string.default_min_games);
            number="1";
        }

//        if(player1NameText.equals("")||player2NameText.equals("")){
//            toastMessage("Please enter names of both the players");
//            return;
//        }
        if(player1NameText.equals("")){
            player1NameText="Human";
            player1NameBox.setText("Human");
        }

        Intent intent = new Intent(this,Game_PlayGameWithComputer.class);
        intent.putExtra("Player 1",player1NameText);
        intent.putExtra("Number",number);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
            finish();
        }
    }
}