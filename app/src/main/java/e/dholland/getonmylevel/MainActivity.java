package e.dholland.getonmylevel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button startButton;
    Button rulesButton;
    Button coopButton;
    Button leaderboardButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rulesButton = (Button)findViewById(R.id.rules_button);
        rulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRules(view);
            }
        });

        startButton = (Button)findViewById(R.id.start_game_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame(view);
            }
        });

        coopButton = (Button)findViewById(R.id.coop_button);
        coopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCoop(view);
            }
        });
        leaderboardButton = (Button)findViewById(R.id.start_leaderboard);
        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLeaderboard(view);
            }
        });
    }
    public void startGame(View view){
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
    public void showRules(View view){
        Intent intent = new Intent(this, RulesActivity.class);
        startActivity(intent);
    }
    public void startCoop(View view){
        Intent intent = new Intent(this, CoopActivity.class);
        startActivity(intent);
    }
    public void startLeaderboard(View view){
        Intent intent = new Intent(this, LeaderboardActivity.class);
        startActivity(intent);
    }
}
