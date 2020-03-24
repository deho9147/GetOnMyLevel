package e.dholland.getonmylevel;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class GameActivity extends AppCompatActivity {
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

    Switch teamSwitch;
    TextView teamGuessingText;
    int teamActive = 1;

    Switch higherLowerSwitch;
    Button scoreButton;
    Button nextTurnButton;

    EditText team1ScoreText;
    int team1Score;
    EditText team2ScoreText;
    int team2Score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_game);

        teamGuessingText = (TextView)findViewById(R.id.team_guessing_text);
        teamSwitch = (Switch)findViewById(R.id.team_switch);
        teamSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton.isPressed()){
                    changeActiveTeam();

                }
            }
        });

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


        team1Score = 0;
        team1ScoreText=(EditText)findViewById(R.id.team1_score);
        team1ScoreText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId== EditorInfo.IME_ACTION_DONE||actionId== EditorInfo.IME_ACTION_NEXT){
                    team1Score=Integer.parseInt(team1ScoreText.getText().toString());
                    team1ScoreText.clearFocus();

                }
                return false;
            }
        });
        team2Score = 0;
        team2ScoreText=(EditText)findViewById(R.id.team2_score);
        team2ScoreText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId== EditorInfo.IME_ACTION_DONE||actionId== EditorInfo.IME_ACTION_NEXT){
                    team2Score=Integer.parseInt(team2ScoreText.getText().toString());
                    team2ScoreText.clearFocus();

                }
                return false;
            }
        });

        higherLowerSwitch = (Switch)findViewById(R.id.higher_lower_switch);
        higherLowerSwitch.setThumbDrawable(getDrawable(R.drawable.switch_thumb_team2));

        scoreButton = (Button)findViewById(R.id.score_button);
        scoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(higherLowerSwitch.isChecked());
                scoreRound();
                hidePointsBar.setProgress(0);
            }
        });
        nextTurnButton = (Button)findViewById(R.id.next_turn_button);
        nextTurnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNewPrompt();
                changeActiveTeam();
                hidePointsBar.setProgress(0);
                hideRevealPointsBar();
                setNewPrompt();
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
            if (higherLowerSwitch.isChecked() == false) {
                if (teamActive == 1) {
                    team2Score = (int) team2Score + 1;
                    team2ScoreText.setText(String.valueOf(team2Score));
                } else if (teamActive == 2) {
                    team1Score = (int) team1Score + 1;
                    team1ScoreText.setText(String.valueOf(team1Score));
                }
            }
            if (guessBar.getProgress() >= (pointsBar.getProgress() - 6)) {
                if (teamActive == 1) {
                    team1Score = team1Score + 3;
                    team1ScoreText.setText(String.valueOf(team1Score));
                } else if (teamActive == 2) {
                    team2Score = team2Score + 3;
                    team2ScoreText.setText(String.valueOf(team2Score));
                }
            } else if (guessBar.getProgress() >= (pointsBar.getProgress() - 10)) {
                    if (teamActive == 1) {
                        team1Score = team1Score + 2;
                        team1ScoreText.setText(String.valueOf(team1Score));
                    } else if (teamActive == 2) {
                        team2Score = team2Score + 2;
                        team2ScoreText.setText(String.valueOf(team2Score));
                    }
            }
        } else if (guessBar.getProgress() > pointsBar.getProgress()+2) {
            if (higherLowerSwitch.isChecked()==true) {
                if (teamActive == 1) {
                    team2Score = (int) team2Score + 1;
                    team2ScoreText.setText(String.valueOf(team2Score));
                } else if (teamActive == 2) {
                    team1Score = (int) team1Score + 1;
                    team1ScoreText.setText(String.valueOf(team1Score));
                }
            }
            if (guessBar.getProgress() <= (pointsBar.getProgress() + 6)) {
                if (teamActive == 1) {
                    team1Score = team1Score + 3;
                    team1ScoreText.setText(String.valueOf(team1Score));
                } else if (teamActive == 2) {
                    team2Score = team2Score + 3;
                    team2ScoreText.setText(String.valueOf(team2Score));
                }
            } else if (guessBar.getProgress() <= (pointsBar.getProgress() + 10)) {
                if (teamActive == 1) {
                    team1Score = team1Score + 2;
                    team1ScoreText.setText(String.valueOf(team1Score));
                } else if (teamActive == 2) {
                    team2Score = team2Score + 2;
                    team2ScoreText.setText(String.valueOf(team2Score));
                }
            }
        } else if (guessBar.getProgress() >= (pointsBar.getProgress()-2)&& guessBar.getProgress() <=(pointsBar.getProgress()+2)) {
                if (teamActive == 1) {
                    team1Score = team1Score + 4;
                    team1ScoreText.setText(String.valueOf(team1Score));
                } else if (teamActive == 2) {
                    team2Score = team2Score + 4;
                    team2ScoreText.setText(String.valueOf(team2Score));
                }
            }
    }
    private void hideRevealPointsBar(){
        if (hidePointsBar.getProgress()==100) {
            hidePointsBar.setProgress(0);
            isHidden = false;
            hiderButton.setText("Hide");
        }
        else{
            hidePointsBar.setProgress(100);
            isHidden = true;
            hiderButton.setText("Show");
        }
    }
    private void changeActiveTeam(){
        if (teamActive == 2) {
            teamActive = 1;
            teamSwitch.setChecked(false);
            teamSwitch.setThumbDrawable(getDrawable(R.drawable.switch_thumb_team1));
            higherLowerSwitch.setThumbDrawable(getDrawable(R.drawable.switch_thumb_team2));
            hidePointsBar.getProgressDrawable().setColorFilter(getColor(R.color.team1Color), PorterDuff.Mode.SRC_IN);
            teamGuessingText.setText("Team 1 is guessing");
        }
        else {
            teamActive = 2;
            teamSwitch.setChecked(true);
            teamSwitch.setThumbDrawable(getDrawable(R.drawable.switch_thumb_team2));
            higherLowerSwitch.setThumbDrawable(getDrawable(R.drawable.switch_thumb_team1));
            hidePointsBar.getProgressDrawable().setColorFilter(getColor(R.color.team2Color), PorterDuff.Mode.SRC_IN);
            teamGuessingText.setText("Team 2 is guessing");
        }

    }

}
