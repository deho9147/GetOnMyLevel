package e.dholland.getonmylevel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RulesActivity extends AppCompatActivity {
    TextView rulesExplanation;
    boolean inTurnExample;
    Button exampleButton;
    Button startGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        inTurnExample = false;
        rulesExplanation = (TextView)findViewById(R.id.rules_explanation_text);
        exampleButton = (Button)findViewById(R.id.example_button);
        exampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inTurnExample==false){
                    inTurnExample=true;
                    rulesExplanation.setText(getString(R.string.rules_example));
                    exampleButton.setText("Show Rules");
                } else {
                    inTurnExample = false;
                    rulesExplanation.setText(getString(R.string.rules_explanation));
                    exampleButton.setText("Example Turn");
                }

            }
        });
        startGameButton = (Button)findViewById(R.id.start_game_rules_button);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame(view);

            }
        });
    }
    private void startGame(View view){
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);

    }
}
