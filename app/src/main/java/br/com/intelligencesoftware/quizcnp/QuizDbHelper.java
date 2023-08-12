package br.com.intelligencesoftware.quizcnp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import br.com.intelligencesoftware.quizcnp.QuizContract.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import static br.com.intelligencesoftware.quizcnp.QuizContract.QuestionTable.COLUMN_SOLVED;
import static br.com.intelligencesoftware.quizcnp.QuizContract.QuestionTable.TABLE_NAME;

public class QuizDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME2 = "SWYK2.db";

    private static final int DATABASE_VERSION = 1;
    private static SQLiteDatabase db;

    private static String DATABASE_PATH = "";
    private final Context dbContext;


    QuizDbHelper(Context context) {
        super(context, DATABASE_NAME2, null, DATABASE_VERSION);
        this.dbContext = context;

    }

    //-----------------------------------------------------------------------------------------------------------------

    private void copyDataBase() throws IOException {

        try {
            if(Build.VERSION.SDK_INT >= 17)
                DATABASE_PATH = dbContext.getApplicationInfo().dataDir+"/databases/";
            else
                DATABASE_PATH = "/data/data"+dbContext.getPackageName()+"/databases/";

            InputStream myInput = dbContext.getAssets().open(DATABASE_NAME2);
            String outFileName = DATABASE_PATH + DATABASE_NAME2;
            OutputStream myOutput = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            myOutput.flush();
            myOutput.close();
            myInput.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public void createDataBase() {

        boolean dbExist = checkDataBase();

        if (dbExist) {

        }
        else {

            this.getReadableDatabase();
            try {
                copyDataBase();
            }
            catch (Exception e) {
            }
        }
    }


    private boolean checkDataBase()
    {
        SQLiteDatabase checkDB = null;
        try {
            if(Build.VERSION.SDK_INT >= 17)
                DATABASE_PATH = dbContext.getApplicationInfo().dataDir+"/databases/";
            else
                DATABASE_PATH = "/data/data"+dbContext.getPackageName()+"/databases/";

            String path = DATABASE_PATH+DATABASE_NAME2;
            checkDB = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE);
        }
        catch (Exception e) {}
        if (checkDB != null)
            checkDB.close();
        return checkDB != null?true:false;

    }


    @Override
    public synchronized void close (){

        if(db != null)
            db.close();
        super.close();

    }

    public void openDataBase(){
        if(Build.VERSION.SDK_INT >= 17)
            DATABASE_PATH = dbContext.getApplicationInfo().dataDir+"/databases/";
        else
            DATABASE_PATH = "/data/data"+dbContext.getPackageName()+"/databases/";

        String path = DATABASE_PATH+DATABASE_NAME2;
        db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE);
    }


    //-----------------------------------------------------------------------------------------------------------------

    @Override
    public void onCreate(SQLiteDatabase db) {


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + QuestionTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QuizContract2.QuestionTable2.TABLE_NAME);
        onCreate(db);

    }


    public boolean updateItem(int code, int correct, int incorrect, int solved) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(QuestionTable.COLUMN_CORRECT, correct);
        contentValues.put(QuestionTable.COLUMN_INCORRECT, incorrect);
        contentValues.put(COLUMN_SOLVED, solved);
        db.update(TABLE_NAME, contentValues, "code=?", new String[]{String.valueOf(code)});
        db.close();
        return true;
    }

    public boolean updateItem2(int code, int correct, int incorrect, int solved) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(QuizContract2.QuestionTable2.COLUMN_CORRECT, correct);
        contentValues.put(QuizContract2.QuestionTable2.COLUMN_INCORRECT, incorrect);
        contentValues.put(QuizContract2.QuestionTable2.COLUMN_SOLVED, solved);
        db.update(QuizContract2.QuestionTable2.TABLE_NAME, contentValues, "code=?", new String[]{String.valueOf(code)});
        db.close();
        return true;
    }

    public boolean resetStat(int correct, int incorrect, int solved) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(QuestionTable.COLUMN_CORRECT, correct);
        contentValues.put(QuestionTable.COLUMN_INCORRECT, incorrect);
        contentValues.put(COLUMN_SOLVED, solved);
        db.update(TABLE_NAME, contentValues, "solved=?", new String[]{String.valueOf(1)});
        db.close();
        return true;
    }

    public boolean resetStat2(int correct, int incorrect, int solved) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(QuizContract2.QuestionTable2.COLUMN_CORRECT, correct);
        contentValues.put(QuizContract2.QuestionTable2.COLUMN_INCORRECT, incorrect);
        contentValues.put(QuizContract2.QuestionTable2.COLUMN_SOLVED, solved);
        db.update(QuizContract2.QuestionTable2.TABLE_NAME, contentValues, "solved=?", new String[]{String.valueOf(1)});
        db.close();
        return true;
    }


    public ArrayList<br.com.intelligencesoftware.quizcnp.Questions> getQuestions() {

        db = getReadableDatabase();
        String sql = "SELECT * FROM " + QuizContract.QuestionTable.TABLE_NAME + " WHERE " + QuestionTable.COLUMN_SOLVED + " =?";
        Cursor c;
        String solvedstring = Integer.toString(0);
        c = db.rawQuery(sql, new String[]{solvedstring});
        ArrayList<br.com.intelligencesoftware.quizcnp.Questions> questionList = new ArrayList<>();

        if (c.moveToFirst()) {
            do {

                Questions question = new Questions();
                question.setCode(c.getInt(c.getColumnIndex(QuestionTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION4)));
                question.setComment(c.getString(c.getColumnIndex(QuestionTable.COLUMN_COMMENT)));
                question.setCorrect(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_CORRECT)));
                question.setIncorrect(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_INCORRECT)));
                question.setSolved(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_SOLVED)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER_NR)));

                questionList.add(question);

            } while (c.moveToNext());

        }
        c.close();
        return questionList;
    }

    public ArrayList<br.com.intelligencesoftware.quizcnp.Questions> getSolvedQuestions() {

        db = getReadableDatabase();
        String sql = "SELECT * FROM " + QuizContract.QuestionTable.TABLE_NAME + " WHERE " + QuestionTable.COLUMN_SOLVED + " =?";
        Cursor c;
        String solvedstring = Integer.toString(1);
        c = db.rawQuery(sql, new String[]{solvedstring});
        ArrayList<br.com.intelligencesoftware.quizcnp.Questions> questionSolvedList = new ArrayList<>();

        if (c.moveToFirst()) {
            do {

                Questions question = new Questions();
                question.setCode(c.getInt(c.getColumnIndex(QuestionTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION4)));
                question.setComment(c.getString(c.getColumnIndex(QuestionTable.COLUMN_COMMENT)));
                question.setCorrect(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_CORRECT)));
                question.setIncorrect(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_INCORRECT)));
                question.setSolved(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_SOLVED)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER_NR)));

                questionSolvedList.add(question);

            } while (c.moveToNext());

        }
        c.close();
        return questionSolvedList;
    }

    public ArrayList<br.com.intelligencesoftware.quizcnp.Questions> getSolvedRightQuestions() {

        db = getReadableDatabase();
        String sql = "SELECT * FROM " + QuizContract.QuestionTable.TABLE_NAME + " WHERE " + QuestionTable.COLUMN_CORRECT + " =?";
        Cursor c;
        String solvedstring = Integer.toString(1);
        c = db.rawQuery(sql, new String[]{solvedstring});
        ArrayList<br.com.intelligencesoftware.quizcnp.Questions> questionSolvedRightList = new ArrayList<>();

        if (c.moveToFirst()) {
            do {

                Questions question = new Questions();
                question.setCode(c.getInt(c.getColumnIndex(QuestionTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION4)));
                question.setComment(c.getString(c.getColumnIndex(QuestionTable.COLUMN_COMMENT)));
                question.setCorrect(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_CORRECT)));
                question.setIncorrect(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_INCORRECT)));
                question.setSolved(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_SOLVED)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER_NR)));

                questionSolvedRightList.add(question);

            } while (c.moveToNext());

        }
        c.close();
        return questionSolvedRightList;
    }

    public ArrayList<br.com.intelligencesoftware.quizcnp.Questions> getSolvedWrongQuestions() {

        db = getReadableDatabase();
        String sql = "SELECT * FROM " + QuizContract.QuestionTable.TABLE_NAME + " WHERE " + QuestionTable.COLUMN_INCORRECT + " =?";
        Cursor c;
        String solvedstring = Integer.toString(1);
        c = db.rawQuery(sql, new String[]{solvedstring});
        ArrayList<br.com.intelligencesoftware.quizcnp.Questions> questionSolvedWrongtList = new ArrayList<>();

        if (c.moveToFirst()) {
            do {

                Questions question = new Questions();
                question.setCode(c.getInt(c.getColumnIndex(QuestionTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION4)));
                question.setComment(c.getString(c.getColumnIndex(QuestionTable.COLUMN_COMMENT)));
                question.setCorrect(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_CORRECT)));
                question.setIncorrect(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_INCORRECT)));
                question.setSolved(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_SOLVED)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER_NR)));

                questionSolvedWrongtList.add(question);

            } while (c.moveToNext());
        }
        return questionSolvedWrongtList;
    }



    public ArrayList<br.com.intelligencesoftware.quizcnp.Questions> gettotalQuestions() {

        db = getReadableDatabase();
        String sql = "SELECT * FROM " + QuizContract.QuestionTable.TABLE_NAME;
        Cursor c;
        c = db.rawQuery(sql, null);
        ArrayList<br.com.intelligencesoftware.quizcnp.Questions> questionTotalList = new ArrayList<>();

        if (c.moveToFirst()) {
            do {

                Questions question = new Questions();
                question.setCode(c.getInt(c.getColumnIndex(QuestionTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION4)));
                question.setComment(c.getString(c.getColumnIndex(QuestionTable.COLUMN_COMMENT)));
                question.setCorrect(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_CORRECT)));
                question.setIncorrect(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_INCORRECT)));
                question.setSolved(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_SOLVED)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER_NR)));

                questionTotalList.add(question);

            } while (c.moveToNext());

        }
        c.close();
        return questionTotalList;
    }

    public ArrayList<br.com.intelligencesoftware.quizcnp.Questions2> getQuestionsasdf2() {

        db = getReadableDatabase();
        String sql = "SELECT * FROM " + QuizContract2.QuestionTable2.TABLE_NAME + " WHERE " + QuizContract2.QuestionTable2.COLUMN_SOLVED + " =?";
        Cursor c;
        String solvedstring = Integer.toString(0);
        c = db.rawQuery(sql, new String[]{solvedstring});
        ArrayList<br.com.intelligencesoftware.quizcnp.Questions2> questionList2 = new ArrayList<>();

        if (c.moveToFirst()) {
            do {

                Questions2 question = new Questions2();
                question.setCode(c.getInt(c.getColumnIndex(QuizContract2.QuestionTable2._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_OPTION2)));
                question.setComment(c.getString(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_COMMENT)));
                question.setCorrect(c.getInt(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_CORRECT)));
                question.setIncorrect(c.getInt(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_INCORRECT)));
                question.setSolved(c.getInt(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_SOLVED)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_ANSWER_NR)));

                questionList2.add(question);

            } while (c.moveToNext());

        }
        c.close();
        return questionList2;
    }


    public ArrayList<br.com.intelligencesoftware.quizcnp.Questions2> getSolvedQuestions2() {

        db = getReadableDatabase();
        String sql = "SELECT * FROM " + QuizContract2.QuestionTable2.TABLE_NAME + " WHERE " + QuizContract2.QuestionTable2.COLUMN_SOLVED + " =?";
        Cursor c;
        String solvedstring = Integer.toString(1);
        c = db.rawQuery(sql, new String[]{solvedstring});
        ArrayList<br.com.intelligencesoftware.quizcnp.Questions2> questionSolvedList2 = new ArrayList<>();

        if (c.moveToFirst()) {
            do {

                Questions2 question = new Questions2();
                question.setCode(c.getInt(c.getColumnIndex(QuizContract2.QuestionTable2._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_OPTION2)));
                question.setComment(c.getString(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_COMMENT)));
                question.setCorrect(c.getInt(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_CORRECT)));
                question.setIncorrect(c.getInt(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_INCORRECT)));
                question.setSolved(c.getInt(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_SOLVED)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_ANSWER_NR)));

                questionSolvedList2.add(question);

            } while (c.moveToNext());

        }



        c.close();
        return questionSolvedList2;
    }

    public ArrayList<br.com.intelligencesoftware.quizcnp.Questions2> getSolvedRightQuestions2() {

        db = getReadableDatabase();
        String sql = "SELECT * FROM " + QuizContract2.QuestionTable2.TABLE_NAME + " WHERE " + QuizContract2.QuestionTable2.COLUMN_CORRECT + " =?";
        Cursor c;
        String solvedstring = Integer.toString(1);
        c = db.rawQuery(sql, new String[]{solvedstring});
        ArrayList<br.com.intelligencesoftware.quizcnp.Questions2> questionSolvedRightList2 = new ArrayList<>();

        if (c.moveToFirst()) {
            do {

                Questions2 question = new Questions2();
                question.setCode(c.getInt(c.getColumnIndex(QuizContract2.QuestionTable2._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_OPTION2)));
                question.setComment(c.getString(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_COMMENT)));
                question.setCorrect(c.getInt(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_CORRECT)));
                question.setIncorrect(c.getInt(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_INCORRECT)));
                question.setSolved(c.getInt(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_SOLVED)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_ANSWER_NR)));

                questionSolvedRightList2.add(question);

            } while (c.moveToNext());
        }
        c.close();
        return questionSolvedRightList2;
    }

    public ArrayList<br.com.intelligencesoftware.quizcnp.Questions2> getSolvedWrongQuestions2() {

        db = getReadableDatabase();
        String sql = "SELECT * FROM " + QuizContract2.QuestionTable2.TABLE_NAME + " WHERE " + QuizContract2.QuestionTable2.COLUMN_INCORRECT + " =?";
        Cursor c;
        String solvedstring = Integer.toString(1);
        c = db.rawQuery(sql, new String[]{solvedstring});
        ArrayList<br.com.intelligencesoftware.quizcnp.Questions2> questionSolvedWrongtList2 = new ArrayList<>();

        if (c.moveToFirst()) {
            do {

                Questions2 question = new Questions2();
                question.setCode(c.getInt(c.getColumnIndex(QuizContract2.QuestionTable2._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_OPTION2)));
                question.setComment(c.getString(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_COMMENT)));
                question.setCorrect(c.getInt(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_CORRECT)));
                question.setIncorrect(c.getInt(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_INCORRECT)));
                question.setSolved(c.getInt(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_SOLVED)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_ANSWER_NR)));

                questionSolvedWrongtList2.add(question);

            } while (c.moveToNext());
        }
        return questionSolvedWrongtList2;
    }



    public ArrayList<br.com.intelligencesoftware.quizcnp.Questions2> gettotalQuestions2() {

        db = getReadableDatabase();
        String sql = "SELECT * FROM " + QuizContract2.QuestionTable2.TABLE_NAME;
        Cursor c;
        c = db.rawQuery(sql, null);
        ArrayList<br.com.intelligencesoftware.quizcnp.Questions2> questionTotalList2 = new ArrayList<>();

        if (c.moveToFirst()) {
            do {

                Questions2 question = new Questions2();
                question.setCode(c.getInt(c.getColumnIndex(QuizContract2.QuestionTable2._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_OPTION2)));
                question.setComment(c.getString(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_COMMENT)));
                question.setCorrect(c.getInt(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_CORRECT)));
                question.setIncorrect(c.getInt(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_INCORRECT)));
                question.setSolved(c.getInt(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_SOLVED)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuizContract2.QuestionTable2.COLUMN_ANSWER_NR)));

                questionTotalList2.add(question);

            } while (c.moveToNext());

        }
        c.close();
        return questionTotalList2;
    }
}