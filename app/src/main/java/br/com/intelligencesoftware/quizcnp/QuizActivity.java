package br.com.intelligencesoftware.quizcnp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;


public class QuizActivity extends AppCompatActivity {

    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private Button buttonConfirmNext;
    private TextView textViewQuestion;
    private TextView textViewQuestionCount;
    private ArrayList<br.com.intelligencesoftware.quizcnp.Questions> questionList;
    private int questionCounter;
    private int questionTotalCount;
    private br.com.intelligencesoftware.quizcnp.Questions currentQuestions;
    private boolean answerd;

    QuizDbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_quiz);
        
        setupUI();
        fetchDB();
        Log.i("BUGBUG","onCreate() in QuizActivity");

        db = new QuizDbHelper(this);

        //+++++++++++++++++++ NEXT BUTTON

        TextView comentario = (TextView)findViewById(R.id.textViewComentarioasdf);

        TextView tv = (TextView)findViewById(R.id.txtViewNext);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuestions();
                comentario.setText(" ");
            }
        });


        TextView comment = (TextView)findViewById(R.id.txtComment);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comentario.setText(currentQuestions.getComment());

            }
        });

        int id = currentQuestions.getCode();
        int correct = 0;
        int incorrect = 0;
        int solved = 0;
        boolean isUpdate = db.updateItem(id, correct, incorrect, solved);

    }


    private void setupUI(){
        textViewQuestion = findViewById(R.id.txtQuestionContainer);
        textViewQuestionCount = findViewById(R.id.txtTotalQuestion);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        rb4 = findViewById(R.id.radio_button4);
        buttonConfirmNext = findViewById(R.id.button);
    }

    public void fetchDB() {
        br.com.intelligencesoftware.quizcnp.QuizDbHelper dbHelper = new br.com.intelligencesoftware.quizcnp.QuizDbHelper(this);
        questionList = dbHelper.getQuestions();
        startQuiz();
    }
    

     public void startQuiz() {

        questionTotalCount = questionList.size();
        showQuestions();

         rbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(RadioGroup group, int checkedId) {

                 switch (checkedId){

                     case R.id.radio_button1:

                         rb1.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                         rb2.setTextColor(Color.BLACK);
                         rb3.setTextColor(Color.BLACK);
                         rb4.setTextColor(Color.BLACK);

                         rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.when_answer_selected2));
                         rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.option_default_background2));
                         rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.option_default_background2));
                         rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.option_default_background2));

                         break;

                     case R.id.radio_button2:
                         rb2.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                         rb1.setTextColor(Color.BLACK);
                         rb3.setTextColor(Color.BLACK);
                         rb4.setTextColor(Color.BLACK);

                         rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.when_answer_selected2));
                         rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.option_default_background2));
                         rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.option_default_background2));
                         rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.option_default_background2));

                         break;

                     case R.id.radio_button3:
                         rb3.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                         rb2.setTextColor(Color.BLACK);
                         rb1.setTextColor(Color.BLACK);
                         rb4.setTextColor(Color.BLACK);

                         rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.when_answer_selected2));
                         rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.option_default_background2));
                         rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.option_default_background2));
                         rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.option_default_background2));

                         break;

                     case R.id.radio_button4:
                         rb4.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                         rb2.setTextColor(Color.BLACK);
                         rb3.setTextColor(Color.BLACK);
                         rb1.setTextColor(Color.BLACK);

                         rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.when_answer_selected2));
                         rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.option_default_background2));
                         rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.option_default_background2));
                         rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.option_default_background2));

                         break;
                 }

             }
         });

         buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!answerd) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {

                        quizOperation();
                    } else {

                        Toast.makeText(QuizActivity.this, "Please Select the Answer ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }


    public void showQuestions() {

        rbGroup.clearCheck();

        rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.option_default_background2));
        rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.option_default_background2));
        rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.option_default_background2));
        rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.option_default_background2));

        rb1.setTextColor(Color.BLACK);
        rb2.setTextColor(Color.BLACK);
        rb3.setTextColor(Color.BLACK);
        rb4.setTextColor(Color.BLACK);


        if (questionCounter < questionTotalCount) {
            currentQuestions = questionList.get(questionCounter);
            textViewQuestion.setText(currentQuestions.getQuestion());
            rb1.setText(currentQuestions.getOption1());
            rb2.setText(currentQuestions.getOption2());
            rb3.setText(currentQuestions.getOption3());
            rb4.setText(currentQuestions.getOption4());

            questionCounter++;
            answerd = false;
            buttonConfirmNext.setText("Confirm");

            textViewQuestionCount.setText("Questions: " + questionCounter + "/" + questionTotalCount);

        } else {

            // If Number of Questions Finishes then we need to finish the Quiz and Shows the User Quiz Performance

            Toast.makeText(this, "Congratulations! You've finished all the questions.", Toast.LENGTH_SHORT).show();

            rb1.setClickable(false);
            rb2.setClickable(false);
            rb3.setClickable(false);
            rb4.setClickable(false);
            buttonConfirmNext.setClickable(false);
        }
    }

    private void quizOperation() {
        answerd = true;

        RadioButton rbselected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbselected) + 1;

        checkSolution(answerNr, rbselected);

    }

    private void checkSolution(int answerNr, RadioButton rbselected) {

        switch (currentQuestions.getAnswerNr()) {
            case 1:
                if (currentQuestions.getAnswerNr() == answerNr) {

                    rb1.setBackground(ContextCompat.getDrawable(
                            this, R.drawable.correct_option_background3));
                    rb1.setTextColor(Color.BLACK);

                    int code = currentQuestions.getCode();
                    int correct = 1;
                    int incorrect = 0;
                    int solved = 1;
                    boolean isUpdate = db.updateItem(code, correct, incorrect, solved);

                } else {

                    changetoIncorrectColor(rbselected);

                    int code = currentQuestions.getCode();
                    int correct = 0;
                    int incorrect = 1;
                    int solved = 1;
                    boolean isUpdate = db.updateItem(code, correct, incorrect, solved);

                }
                break;
            case 2:
                if (currentQuestions.getAnswerNr() == answerNr) {

                    rb2.setBackground(ContextCompat.getDrawable(this, R.drawable.correct_option_background3));
                    rb2.setTextColor(Color.BLACK);

                    int code = currentQuestions.getCode();
                    int correct = 1;
                    int incorrect = 0;
                    int solved = 1;
                    boolean isUpdate = db.updateItem(code, correct, incorrect, solved);

                } else {

                    changetoIncorrectColor(rbselected);

                    int code = currentQuestions.getCode();
                    int correct = 0;
                    int incorrect = 1;
                    int solved = 1;
                    boolean isUpdate = db.updateItem(code, correct, incorrect, solved);

                }
                break;
            case 3:
                if (currentQuestions.getAnswerNr() == answerNr) {

                    rb3.setBackground(ContextCompat.getDrawable(this, R.drawable.correct_option_background3));
                    rb3.setTextColor(Color.BLACK);

                    int code = currentQuestions.getCode();
                    int correct = 1;
                    int incorrect = 0;
                    int solved = 1;
                    boolean isUpdate = db.updateItem(code, correct, incorrect, solved);

                } else {

                    changetoIncorrectColor(rbselected);

                    int code = currentQuestions.getCode();
                    int correct = 0;
                    int incorrect = 1;
                    int solved = 1;
                    boolean isUpdate = db.updateItem(code, correct, incorrect, solved);

                }
                break;
            case 4:
                if (currentQuestions.getAnswerNr() == answerNr) {

                    rb4.setBackground(ContextCompat.getDrawable(this, R.drawable.correct_option_background3));
                    rb4.setTextColor(Color.BLACK);

                    int code = currentQuestions.getCode();
                    int correct = 1;
                    int incorrect = 0;
                    int solved = 1;
                    boolean isUpdate = db.updateItem(code, correct, incorrect, solved);

                } else {

                    changetoIncorrectColor(rbselected);

                    int code = currentQuestions.getCode();
                    int correct = 0;
                    int incorrect = 1;
                    int solved = 1;
                    boolean isUpdate = db.updateItem(code, correct, incorrect, solved);

                }
                break;
        }
        if (questionCounter == questionTotalCount) {
            buttonConfirmNext.setText("Confirm");
        }
    }


    void changetoIncorrectColor(RadioButton rbselected) {
        rbselected.setBackground(ContextCompat.getDrawable(this, R.drawable.wrong_answer_background2));
        rbselected.setTextColor(Color.BLACK);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("BUGBUG","onRestart() in QuizActivity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("BUGBUG","onStop() in QuizActivity");
        finish();

    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i("BUGBUG","onPause() in QuizActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("BUGBUG","onResume() in QuizActivity");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("BUGBUG","onStart() in QuizActivity");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(QuizActivity.this,PlayActivity.class);
        startActivity(intent);
    }

}
