package edu.miracosta.cs134;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.miracosta.cs134.model.JSONLoader;
import edu.miracosta.cs134.model.Student;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Superheroe Quiz!";
    private static final int STUDENTS_IN_QUIZ = 10;

    private Button[] mButtons = new Button[4];
    private List<Student> mAllStudentsList;         // all the students loaded from JSON
    private List<Student> mQuizStudentsList;        // students in the current quiz (only 10)
    private Student mCorrectStudent;                // correct student for the current question
    private int mTotalGuesses;                      // number of total guesses made
    private int mCorrectGuesses;                    // number of correct guesses made
    private SecureRandom rng; // used to randomize the quiz
    private Handler handler; // used to delay loading the next student

    private TextView mQuestionNumberTextView;       // shows the current question number
    private ImageView mStudentImageView;            // displays a student
    private TextView mAnswerTextView;               // displays correct answer

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQuizStudentsList = new ArrayList<>(STUDENTS_IN_QUIZ);
        rng = new SecureRandom();
        handler = new Handler();

        // Get references to GUI Components (text views and image view)
        mQuestionNumberTextView = findViewById(R.id.questionNumberTextView);
        mStudentImageView = findViewById(R.id.studentImageView);
        mAnswerTextView = findViewById(R.id.answerTextView);

        // put all 4 butons in the array (mButtons)
        mButtons[0] = findViewById(R.id.button);
        mButtons[1] = findViewById(R.id.button2);
        mButtons[2] = findViewById(R.id.button3);
        mButtons[3] = findViewById(R.id.button4);

        // set mQuestionNumberTextView's text to the appropriate strings.xml resource
        mQuestionNumberTextView.setText("Question {1} of {10}");

        // load all the countries from the JSON file using the JSONLoader
        try {
            mAllStudentsList = JSONLoader.loadJSONFromAsset(this);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        // now call the method resetQuiz to the start the quiz
        resetQuiz();
    }

    /**
     * Sets up and starts a new quiz
     */
    public void resetQuiz() {
        // reset the number of correct guesses made
        mCorrectGuesses = 0;
        // reset the total number of guesses the user made
        mTotalGuesses = 0;
        // clear the list of quiz countries (for prior games played)
        mQuizStudentsList.clear();
        // randomly add STUDENTS_IN_QUIZ (10) students from the mAllStudentSList
        int size = mAllStudentsList.size();
        int randomPosition;
        Student randomStudent;
        while(mQuizStudentsList.size() <= STUDENTS_IN_QUIZ) {
            Log.e(TAG, "Calling RNG with size: " + size);
            randomPosition = rng.nextInt(size);
            randomStudent = mAllStudentsList.get(randomPosition);

            // check for duplicates
            if (!mQuizStudentsList.contains(randomStudent))
                mQuizStudentsList.add(randomStudent);
        }

        // Lets set the text of the 4 buttons to the first 4 superpowers
        // names, or "one thing"s
        // TODO: Implement this using the settings we configure
        for (int i = 0; i < mButtons.length; i++) {
            mButtons[i].setText(mQuizStudentsList.get(i).getName());
        }

        // start the quiz by calling loadNextStudent
        loadNextStudent();
    }

    /**
     * Method initiates the proccess of loading the next flag for the quiz, showing
     * the flags image and then 4 buttons, one of which contains the correct answer.
     */
    private void loadNextStudent() {
        // initialize the mCorrectStudent by removing the item at position 0 in mQuizStudents list
        mCorrectStudent = mQuizStudentsList.get(0);
        mQuizStudentsList.remove(0);

        // clear the mAnswerTextView so that it doesnt show text from the previous
        mAnswerTextView.setText("");

        // display the current question number in the mQuestionNumberTextView
        mQuestionNumberTextView.setText("Question {mCorrectGuesses} of {10}");

        // use asset manager to load the next image from the assets folder
        AssetManager am = getAssets();

        try {
            InputStream stream = am.open(mCorrectStudent.getFileName());
            Drawable image = Drawable.createFromStream(stream, mCorrectStudent.getName());
            mStudentImageView.setImageDrawable(image);
        } catch (IOException e) {
            Log.e("Image trouble!", e.getMessage());
        }

        // shuffle the orer of all the students (using collections.shuffle)
        do {
            Collections.shuffle(mAllStudentsList);
        } while( mAllStudentsList.subList(0, mButtons.length).contains(mCorrectStudent));

        // loop through all 4 buttons, enable them all
        for (int i = 0; i < mButtons.length; i++) {
            mButtons[i].setEnabled(true);

            // TODO: Implement this by using the settings we configure
            mButtons[i].setText(mAllStudentsList.get(i).getName());
        }

        // after the loop, randomly replace one of the 4 buttons with the text of the correct
        // answer type
        // TODO: Implement this LINE
        mButtons[rng.nextInt(mButtons.length)].setText(mCorrectStudent.getName());
    }

    public void makeGuess(View v) {
        mTotalGuesses++;
        // downcast the view into a button
        Button clickedButton = (Button) v;
        String guessedText = clickedButton.getText().toString();

        // if the guess matches the countries correct component, incremnt correct guesses
        // TODO: IMPLEMENT THIS LINE
        if (guessedText.equalsIgnoreCase(mCorrectStudent.getName())) {
            mCorrectGuesses++;

            // but the game isnt over yet
            if (mCorrectGuesses < STUDENTS_IN_QUIZ) {
                // disable all buttons
                for (int i = 0; i < mButtons.length; i++) {
                    mButtons[i].setEnabled(false);
                }

                // change the answer text to correct answer
                // make the text green
                // TODO: implement this line
                mAnswerTextView.setText(mCorrectStudent.getName());
                mAnswerTextView.setTextColor(getResources().getColor(R.color.correct_answer));

                // call load next flag
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextStudent();
                    }
                }, 1500);
            } else {
                // game over
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                double percentage = (double) mCorrectGuesses / mTotalGuesses * 100.0;
                builder.setMessage("{mTotalGuesses} guesses, {percentage}%% correct");
                builder.setPositiveButton("Reset Quiz",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                resetQuiz();
                            }
                        });

                // disable the cancel operation
                builder.setCancelable(false);
                builder.create();
                builder.show();
            }
        } else {
            // incorrect guess. disable the button
            clickedButton.setEnabled(false);
            mAnswerTextView.setText("Incorrect!");
            mAnswerTextView.setTextColor(getResources().getColor(R.color.incorrect_answer));
        }
    } // end of method
}
