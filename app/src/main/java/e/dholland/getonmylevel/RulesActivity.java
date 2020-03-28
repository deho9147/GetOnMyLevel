package e.dholland.getonmylevel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RulesActivity extends AppCompatActivity {
    TextView rulesExplanationUpper;
    TextView rulesExplanationLower;
    TextView rulesTitleUpper;
    TextView rulesTitleLower;
    ImageView levelImg;
    ImageView tunerImg;
    int rulesPageNumber;
    Button backButton;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        rulesPageNumber = 0;
        rulesExplanationUpper = (TextView)findViewById(R.id.rules_explanation_text);
        rulesExplanationLower = (TextView)findViewById(R.id.explanation_text_lower);
        rulesTitleLower=(TextView)findViewById(R.id.page_title_bottom);
        rulesTitleUpper=(TextView)findViewById(R.id.page_title);
        levelImg = (ImageView)findViewById(R.id.levelImg);
        tunerImg = (ImageView)findViewById(R.id.tunerImg);

        rulesTitleLower.setVisibility(View.GONE);
        rulesExplanationLower.setVisibility(View.GONE);
        rulesSummary();


        backButton= (Button)findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rulesPageNumber == 0){
                    //go home
                    startMenu(view);
                }
                else{
                    rulesPageNumber = rulesPageNumber-1;
                }
                if (rulesPageNumber==0){
                    rulesSummary();
                } else if(rulesPageNumber==1){
                    levelTunerExplanation();
                }else if(rulesPageNumber==2){
                    clueGiverRules();
                }else if(rulesPageNumber==3){
                    sliderPromptExplanation();
                }else if(rulesPageNumber==4){
                    scoreTurnExplanation();
                }else if(rulesPageNumber==5){
                    turnExample();
                }

            }
        });
        nextButton = (Button)findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rulesPageNumber == 5){
                    //go home
                    startGame(view);
                }
                else{
                    rulesPageNumber = rulesPageNumber+1;
                }
                if (rulesPageNumber==0){
                    rulesSummary();
                } else if(rulesPageNumber==1){
                    levelTunerExplanation();
                }else if(rulesPageNumber==2){
                    clueGiverRules();
                }else if(rulesPageNumber==3){
                    sliderPromptExplanation();
                }else if(rulesPageNumber==4){
                    scoreTurnExplanation();
                }else if(rulesPageNumber==5){
                    turnExample();
                }

            }
        });
    }
    private void startGame(View view){
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);

    }
    private void startMenu(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void rulesSummary(){
        rulesTitleUpper.setVisibility(View.VISIBLE);
        rulesTitleLower.setVisibility(View.GONE);
        rulesExplanationLower.setVisibility(View.GONE);
        levelImg.setVisibility(View.GONE);
        tunerImg.setVisibility(View.GONE);
        rulesTitleUpper.setText("Summary");
        rulesExplanationUpper.setText(R.string.rules_summary);
    }
    private void levelTunerExplanation(){
        rulesTitleLower.setVisibility(View.GONE);
        rulesExplanationLower.setVisibility(View.VISIBLE);
        rulesExplanationLower.setText(R.string.tuner_explanation);
        rulesTitleLower.setText("Tuner");
        tunerImg.setVisibility(View.VISIBLE);
        levelImg.setVisibility(View.VISIBLE);
        rulesTitleUpper.setVisibility(View.GONE);
        rulesExplanationUpper.setText(R.string.level_explanation);
    }
    private void clueGiverRules(){
        rulesTitleUpper.setText("Clue Giver");
        rulesTitleUpper.setVisibility(View.VISIBLE);
        rulesTitleLower.setVisibility(View.VISIBLE);
        rulesExplanationUpper.setText(R.string.clue_giver_explanation);
        rulesExplanationLower.setVisibility(View.VISIBLE);
        rulesExplanationLower.setText(R.string.clue_rules);
        rulesTitleLower.setText("Clue Rules");
        tunerImg.setVisibility(View.GONE);
        levelImg.setVisibility(View.GONE);
    }
    private void sliderPromptExplanation(){
        rulesTitleLower.setVisibility(View.VISIBLE);
        rulesExplanationLower.setVisibility(View.VISIBLE);
        rulesExplanationLower.setText(R.string.new_prompt_explanation);
        rulesTitleLower.setText("New Prompt");
        tunerImg.setVisibility(View.GONE);
        levelImg.setVisibility(View.GONE);
        rulesTitleUpper.setVisibility(View.VISIBLE);
        rulesTitleUpper.setText("Lower vs Higher");
        rulesExplanationUpper.setText(R.string.higher_lower_explanation);
    }
    private void scoreTurnExplanation(){
        rulesTitleLower.setVisibility(View.VISIBLE);
        rulesExplanationLower.setVisibility(View.VISIBLE);
        rulesExplanationLower.setText(R.string.next_turn_explanation);
        rulesTitleLower.setText("Next Turn");
        tunerImg.setVisibility(View.GONE);
        levelImg.setVisibility(View.GONE);
        rulesTitleUpper.setVisibility(View.VISIBLE);
        rulesTitleUpper.setText("Score");
        rulesExplanationUpper.setText(R.string.score_button_explanation);
        nextButton.setText("Next");
    }
    private void turnExample(){
        rulesTitleLower.setVisibility(View.GONE);
        rulesExplanationLower.setVisibility(View.GONE);
        levelImg.setVisibility(View.GONE);
        tunerImg.setVisibility(View.GONE);
        rulesTitleUpper.setText("Example Turn");
        rulesExplanationUpper.setText(R.string.rules_example);
        nextButton.setText("Play");
    }
}
