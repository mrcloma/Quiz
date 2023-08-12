package br.com.intelligencesoftware.quizcnp;

import android.provider.BaseColumns;

public final class QuizContract {

    public QuizContract(){

    }

     public static class QuestionTable implements BaseColumns {

         public static final String TABLE_NAME = "quiz_questions";
         public static final String _ID = "code";
         public static final String COLUMN_QUESTION = "question";
         public static final String COLUMN_OPTION1 = "option1";
         public static final String COLUMN_OPTION2 = "option2";
         public static final String COLUMN_OPTION3 = "option3";
         public static final String COLUMN_OPTION4 = "option4";
         public static final String COLUMN_COMMENT = "comment";
         public static final String COLUMN_CORRECT = "correct";
         public static final String COLUMN_INCORRECT = "incorrect";
         public static final String COLUMN_SOLVED = "solved";
         public static final String COLUMN_ANSWER_NR = "answer_nr";
    }
}
