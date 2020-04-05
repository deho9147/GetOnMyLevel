package e.dholland.getonmylevel;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.awt.font.TextAttribute;
import java.lang.reflect.Type;
import java.util.*;

public class LeaderboardActivity extends AppCompatActivity {
    SharedPreferences sharedPref;
    SharedPreferences.Editor sharedPrefEditor;
    List<Pair> scorePairs;
    String jsonString;
    Pair<String, Integer> addPair;
    Gson gson;


    TextView leaderboardTeamName;
    TextView leaderboardPoints;
    TextView scoredText;

    EditText teamNameInput;
    Button saveButton;

    int teamScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        scorePairs=new ArrayList<Pair>();
        System.out.println(scorePairs);
        teamNameInput = (EditText)findViewById(R.id.teamname_input);
        teamNameInput.setVisibility(View.INVISIBLE);
        scoredText=(TextView)findViewById(R.id.scored_text);
        scoredText.setVisibility(View.INVISIBLE);
        saveButton = (Button)findViewById(R.id.save_button);
        saveButton.setVisibility(View.INVISIBLE);
        if (getIntent().getExtras()!= null){
            teamScore = getIntent().getExtras().getInt("teamScore");
            String temp = "";
            if (teamScore == 1){
                temp = "You scored 1 point!";
            }
            else {
                temp = "You scored " + (Integer.toString(teamScore)) + " points!";
            }
            scoredText.setText(temp);
            teamNameInput.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.VISIBLE);
            scoredText.setVisibility(View.VISIBLE);
        }
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String teamNameTemp = teamNameInput.getText().toString();
                addScore(teamNameTemp,teamScore);
                restartLeaderboard(view);
            }
        });
        readScore();
        leaderboardTeamName=(TextView)findViewById(R.id.leader_list);
        leaderboardPoints=(TextView)findViewById(R.id.leader_points_list);
        displayLeaderboard();
    }
    private void readScore(){
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.SharedPreferenceKey), MODE_PRIVATE);
        jsonString = sharedPref.getString(getString(R.string.ScoreListKey),"");
        gson = new Gson();
        Type type = new TypeToken<List<Pair<String, Integer>>>(){}.getType();
        if (jsonString!= ""){
            scorePairs = gson.fromJson(jsonString,type);
        }else{
            scorePairs = new ArrayList<Pair>();
        }
        System.out.println(scorePairs);
    }
    private void addScore(String teamName, int score){
        addPair = new Pair<> (teamName,score);
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.SharedPreferenceKey), MODE_PRIVATE);
        jsonString = sharedPref.getString(getString(R.string.ScoreListKey),"");
        gson = new Gson();
        scorePairs = new ArrayList<Pair>();
        Type type = new TypeToken<List<Pair>>(){}.getType();
        System.out.println(jsonString);
        if (jsonString!=""){
            scorePairs = gson.fromJson(jsonString, type);
            int length=scorePairs.size();
            System.out.println("HEREREEREREREERE");
            for(int i = 0; i<length; i++){
                System.out.println(scorePairs.get(i));
                if (((int)Float.parseFloat(scorePairs.get(i).second.toString())) <= score){
                    scorePairs.add(i,addPair);
                    break;
                }
            }
        }
        else {
            scorePairs = new ArrayList<Pair>();
            scorePairs.add(addPair);
        }

        jsonString = gson.toJson(scorePairs);
        sharedPrefEditor = sharedPref.edit();
        sharedPrefEditor.putString(getString(R.string.ScoreListKey),jsonString);
        sharedPrefEditor.commit();
    }
    private void displayLeaderboard(){
        String teamNameLeaderboardString = "";
        String pointsLeaderboardString="";
        for(int i = 0; i<scorePairs.size(); i++){
            teamNameLeaderboardString = teamNameLeaderboardString + scorePairs.get(i).first.toString()+"\n";
            pointsLeaderboardString = pointsLeaderboardString + scorePairs.get(i).second.toString()+"\n";
        }
        leaderboardTeamName.setText(teamNameLeaderboardString);
        leaderboardPoints.setText(pointsLeaderboardString);

    }


    private void restartLeaderboard(View view){
        Intent intent = new Intent(this, LeaderboardActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
