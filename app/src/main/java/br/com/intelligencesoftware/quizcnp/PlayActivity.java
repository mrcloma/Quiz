package br.com.intelligencesoftware.quizcnp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity {

    private final static int EXIT_CODE = 100;

    br.com.intelligencesoftware.quizcnp.QuizDbHelper dbHelper = new br.com.intelligencesoftware.quizcnp.QuizDbHelper(this);
    private long backPressedTime;
    private ArrayList<br.com.intelligencesoftware.quizcnp.Questions> questionSolvedList;
    private ArrayList<br.com.intelligencesoftware.quizcnp.Questions2> questionSolvedList2;
    private ArrayList<Questions> questionTotalList;
    private ArrayList<Questions2> questionTotalList2;
    private ArrayList<Questions> questionList;
    private ArrayList<Questions2> questionList2;
    private ArrayList<Questions> questionSolvedRightList;
    private ArrayList<Questions2> questionSolvedRightList2;
    private ArrayList<Questions> questionSolvedWrongList;
    private ArrayList<Questions2> questionSolvedWrongList2;
    private int questionCounter;
    private int questionCertas;
    private int questionErradas;
    private int questionSolvedCounter;
    private TextView textViewQuestionCount;
    private TextView textViewCertasCount;
    private TextView textViewErradasCount;
    private int questionCounter2;
    private int questionCertas2;
    private int questionErradas2;
    private int questionSolvedCounter2;
    private TextView textViewQuestionCount2;
    private TextView textViewCertasCount2;
    private TextView textViewErradasCount2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_play);
        dbHelper.createDataBase();
        fetchDB();
        fetchDB2();
        setUpUI();
        
        Button btPlay = findViewById(R.id.bt_playbutton);
        Button btPlay2 = findViewById(R.id.bt_playbutton2);
        Button btReset1 = findViewById(R.id.playbuttonresetmc);
        Button btReset2 = findViewById(R.id.playbuttonresetta);

        
        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(questionSolvedCounter >= questionCounter){

                    Toast.makeText(getApplicationContext(), "Press Reset Status to restart", Toast.LENGTH_SHORT).show();

                }else {

                    Intent intent = new Intent(PlayActivity.this, br.com.intelligencesoftware.quizcnp.QuizActivity.class);
                    startActivity(intent);

                }

            }
        });


        btReset1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.resetStat(0,0,0);
                GotoPlayActivity();

            }
        });


        btReset2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.resetStat2(0,0,0);
                GotoPlayActivity();

            }
        });


        btPlay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(questionSolvedCounter2 >= questionCounter2){

                    Toast.makeText(getApplicationContext(), "Press Reset Status to restart", Toast.LENGTH_SHORT).show();

                }else {

                    Intent intent2 = new Intent(PlayActivity.this, br.com.intelligencesoftware.quizcnp.QuizActivity2.class);
                    startActivity(intent2);

                }
            }
        });

    }


    private void GotoPlayActivity() {

        startActivityForResult( new Intent(PlayActivity.this,PlayActivity.class),EXIT_CODE);

    }


    public void fetchDB() {

        br.com.intelligencesoftware.quizcnp.QuizDbHelper dbHelper = new br.com.intelligencesoftware.quizcnp.QuizDbHelper(this);
        questionSolvedList = dbHelper.getSolvedQuestions();
        questionTotalList = dbHelper.gettotalQuestions();
        questionList = dbHelper.getQuestions();
        questionSolvedRightList = dbHelper.getSolvedRightQuestions();
        questionSolvedWrongList = dbHelper.getSolvedWrongQuestions();


    }


    public void fetchDB2() {
        br.com.intelligencesoftware.quizcnp.QuizDbHelper dbHelper2 = new br.com.intelligencesoftware.quizcnp.QuizDbHelper(this);
        questionSolvedList2 = dbHelper2.getSolvedQuestions2();
        questionTotalList2 = dbHelper2.gettotalQuestions2();
        questionList2 = dbHelper2.getQuestionsasdf2();
        questionSolvedRightList2 = dbHelper2.getSolvedRightQuestions2();
        questionSolvedWrongList2 = dbHelper2.getSolvedWrongQuestions2();
    }


    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {

            new AlertDialog.Builder(this)
                    .setTitle("Do you  want to exit?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            setResult(RESULT_OK, new Intent().putExtra("Exit", true));
                            finish();
                        }
                    }).create().show();

        }else  {

            Toast.makeText(this, "Press Again to Exit", Toast.LENGTH_SHORT).show();

        }

        backPressedTime = System.currentTimeMillis();

    }


    @Override
    protected void onStop() {

        super.onStop();
        Log.i("BUGBUG","onStop() in PlayActivity");
        finish();

    }


    private void setUpUI(){
        questionSolvedCounter = questionSolvedList.size();
        questionSolvedCounter2 = questionSolvedList2.size();
        questionCounter = questionTotalList.size();
        questionCounter2 = questionTotalList2.size();
        questionCertas = questionSolvedRightList.size();
        questionCertas2 = questionSolvedRightList2.size();
        questionErradas = questionSolvedWrongList.size();
        questionErradas2 = questionSolvedWrongList2.size();
        textViewQuestionCount = findViewById(R.id.textViewTotalMC);
        textViewQuestionCount2 = findViewById(R.id.textViewTotalTA);
        textViewCertasCount = findViewById(R.id.textViewCertasMC);
        textViewCertasCount2 = findViewById(R.id.textViewCertasTA);
        textViewErradasCount = findViewById(R.id.textViewErradasMC);
        textViewErradasCount2 = findViewById(R.id.textViewErradasTA);
        textViewQuestionCount.setText("Status: " + questionSolvedCounter + "/" + questionCounter);
        textViewQuestionCount2.setText("Status: " + questionSolvedCounter2 + "/" + questionCounter2);
        textViewCertasCount.setText("Correct: " + questionCertas);
        textViewCertasCount2.setText("Correct: " + questionCertas2);
        textViewErradasCount.setText("Incorrect: " + questionErradas);
        textViewErradasCount2.setText("Incorrect: " + questionErradas2);

    }
}

