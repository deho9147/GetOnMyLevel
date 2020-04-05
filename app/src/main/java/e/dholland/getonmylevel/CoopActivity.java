package e.dholland.getonmylevel;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class CoopActivity extends AppCompatActivity {
    SeekBar guessBar;
    SeekBar pointsBar;
    ProgressBar hidePointsBar;
    int pointPosition;
    boolean isHidden;
    //When bar width is 700 each progress point counts as 7, with that being the case the center should be an odd number of progress points.
    //For example and in practice with each bar being 28dp wide the center should either be 35 or 21 wide and so on.

    Button hiderButton;
    Button newPromptButton;

    TextView leftDescriptor;
    TextView rightDescriptor;
    TextView centerPrompt;
    private String[] leastMostPrompts;
    private String[] notWeirdWeirdPrompts;
    private String[] worstBestPrompts;

    Button scoreButton;
    Button nextTurnButton;

    EditText roundText;
    int roundNumber;
    EditText teamScoreText;
    int teamScore;

    Button testButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_coop);

        guessBar = (SeekBar) findViewById(R.id.guesserBar);
        guessBar.setProgress(50);

        pointsBar = (SeekBar) findViewById(R.id.pointsBar);
        pointPosition= (int) (Math.random()*(100.0));
        pointsBar.setProgress(pointPosition);
        pointsBar.setEnabled(false);

        hidePointsBar = (ProgressBar)findViewById(R.id.cover_bar);
        hidePointsBar.getProgressDrawable().setColorFilter(getColor(R.color.team1Color), PorterDuff.Mode.SRC_IN);
        hidePointsBar.setProgress(100);

        hiderButton = (Button)findViewById(R.id.hide_show_points_button);
        hiderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideRevealPointsBar();
            }
        });

        centerPrompt=(TextView)findViewById(R.id.center_prompt);
        leftDescriptor=(TextView)findViewById(R.id.left_descriptor);
        rightDescriptor=(TextView)findViewById(R.id.right_descriptor);
        leastMostPrompts=getResources().getStringArray(R.array.leastMost);
        notWeirdWeirdPrompts=getResources().getStringArray(R.array.notWeirdtoWeird);
        worstBestPrompts=getResources().getStringArray(R.array.worsttoBest);
        setNewPrompt();

        newPromptButton = (Button)findViewById(R.id.new_prompt_button);
        newPromptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNewPrompt();
            }
        });


        roundNumber = 1;
        roundText=(EditText)findViewById(R.id.round_counter);
        roundText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId== EditorInfo.IME_ACTION_DONE||actionId== EditorInfo.IME_ACTION_NEXT){
                    roundNumber=Integer.parseInt(roundText.getText().toString());
                    roundText.clearFocus();

                }
                return false;
            }
        });
        teamScore = 0;
        teamScoreText=(EditText)findViewById(R.id.team_score);
        teamScoreText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId== EditorInfo.IME_ACTION_DONE||actionId== EditorInfo.IME_ACTION_NEXT){
                    teamScore=Integer.parseInt(teamScoreText.getText().toString());
                    teamScoreText.clearFocus();

                }
                return false;
            }
        });

        scoreButton = (Button)findViewById(R.id.score_button);
        scoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreRound();
                hideRevealPointsBar();
            }
        });
        nextTurnButton = (Button)findViewById(R.id.next_turn_button);
        nextTurnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (roundNumber<9) {
                    setNewPrompt();
                    hidePointsBar.setProgress(0);
                    hideRevealPointsBar();
                    setNewPrompt();
                    roundNumber = roundNumber + 1;
                    roundText.setText(String.valueOf(roundNumber));
                }else if (roundNumber == 9){
                    setNewPrompt();
                    hidePointsBar.setProgress(0);
                    hideRevealPointsBar();
                    setNewPrompt();
                    roundNumber = roundNumber + 1;
                    roundText.setText(String.valueOf(roundNumber));
                    nextTurnButton.setText("End Game");
                }else{
                    endGameScreen(view);
                }

            }
        });

    }

    private void setNewPrompt(){
        int randPromptIndex=new Random().nextInt((leastMostPrompts.length + worstBestPrompts.length + notWeirdWeirdPrompts.length));
        if (randPromptIndex < leastMostPrompts.length){
            String prompt = leastMostPrompts[randPromptIndex];
            centerPrompt.setText(prompt);
            leftDescriptor.setText("Least");
            rightDescriptor.setText("Most");
        } else if((randPromptIndex >= leastMostPrompts.length)&&(randPromptIndex<(leastMostPrompts.length+worstBestPrompts.length))) {
            String prompt = worstBestPrompts[randPromptIndex-leastMostPrompts.length];
            centerPrompt.setText(prompt);
            leftDescriptor.setText("Worst");
            rightDescriptor.setText("Best");
        } else if(randPromptIndex>=(leastMostPrompts.length+worstBestPrompts.length)){
            String prompt = notWeirdWeirdPrompts[randPromptIndex-leastMostPrompts.length-worstBestPrompts.length];
            centerPrompt.setText(prompt);
            leftDescriptor.setText("Not Weird");
            rightDescriptor.setText("Weird");
        }
        pointPosition= (int) (Math.random()*(100.0));
        pointsBar.setProgress(pointPosition);
        guessBar.setProgress(50);
    }

    private void scoreRound() {
        if (guessBar.getProgress() < (pointsBar.getProgress()-2)) {
            //Other team gets to guess whether other team was higher or lower
            if (guessBar.getProgress() >= (pointsBar.getProgress() - 6)) {
                teamScore = teamScore + 3;
                teamScoreText.setText(String.valueOf(teamScore));
            } else if (guessBar.getProgress() >= (pointsBar.getProgress() - 10)) {
                teamScore = teamScore + 2;
                teamScoreText.setText(String.valueOf(teamScore));
            }
        } else if (guessBar.getProgress() > pointsBar.getProgress()+2) {
            if (guessBar.getProgress() <= (pointsBar.getProgress() + 6)) {
                teamScore = teamScore + 3;
                teamScoreText.setText(String.valueOf(teamScore));
            } else if (guessBar.getProgress() <= (pointsBar.getProgress() + 10)) {
                teamScore = teamScore + 2;
                teamScoreText.setText(String.valueOf(teamScore));
            }
        } else if (guessBar.getProgress() >= (pointsBar.getProgress()-2)&& guessBar.getProgress() <=(pointsBar.getProgress()+2)) {
                teamScore = teamScore + 4;
                teamScoreText.setText(String.valueOf(teamScore));
        }
    }

    private void hideRevealPointsBar(){
        if (hidePointsBar.getProgress()==100) {
            hidePointsBar.setProgress(0);
            isHidden = false;
            hiderButton.setText("Hide");
            guessBar.setEnabled(false);
        }
        else{
            hidePointsBar.setProgress(100);
            isHidden = true;
            hiderButton.setText("Show");
            guessBar.setEnabled(true);
        }
    }
    private void endGameScreen(View view){
        Intent intent = new Intent(this, LeaderboardActivity.class);
        intent.putExtra("teamScore", teamScore);
        startActivity(intent);
    }

}
