package com.bignerdranch.android.geoquiz

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.appbar.AppBarLayout


// determine the source of the package
private const val TAG = "MAINACTIVITY"
private const val KEY_INDEX = "index"
private const val KEY_ENABLE = "buttonStatus"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {
    // stopped on this section
    // https://learning.oreilly.com/library/view/android-programming-the/9780135257555/ch03s05.html


    // lateinit is used to indicate to teh compiler that it is a non-null view
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var prevButton:Button
    private lateinit var cheatButton: Button


    private val quizViewModel: QuizViewModel by lazy{
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

//        val provider: ViewModelProvider = ViewModelProviders.of(this)
//        val quizViewModel = provider.get(QuizViewModel::class.java)
//        Log.d(TAG, "Got a QuizViewMode: $quizViewModel")

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView =findViewById(R.id.question_text_view)
        prevButton = findViewById(R.id.previous_button)
        cheatButton = findViewById(R.id.cheat_button)

        enablePreviousButton()

        trueButton.setOnClickListener { view: View ->
           checkAnswer(true)
        }

        falseButton.setOnClickListener{ view:View->
          checkAnswer(false)
        }

        nextButton.setOnClickListener{view:View->
            quizViewModel.moveToNext();
            updateQuestion()
            enablePreviousButton()
            quizViewModel.isCheater =false
            quizViewModel.enableButton = true
            enableTrueFalseButton()
        }

        questionTextView.setOnClickListener{view:View->
            quizViewModel.moveToNext();
            updateQuestion()
            enablePreviousButton()
            quizViewModel.isCheater =false
            quizViewModel.enableButton = true
            enableTrueFalseButton()
        }

        cheatButton.setOnClickListener{

            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }

        prevButton.setOnClickListener{view:View->
           var previousQuestion = quizViewModel.previousQuestion()
            if(previousQuestion){
                updateQuestion()
            }else{
                Toast.makeText(this, "Can not go back", Toast.LENGTH_SHORT).show()
            }


        }

        updateQuestion()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode != Activity.RESULT_OK){
            return
        }
        if(requestCode == REQUEST_CODE_CHEAT){
            quizViewModel.isCheater =
                    data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")

    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "OnPause() called")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle){
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
        //savedInstanceState.putInt(KEY_ENABLE, quizViewModel.cu
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "OnDestroy() called")
    }


    private fun updateQuestion(){
        enableTrueFalseButton()
        nextButton.setText("Next")
        val questionTextResID = quizViewModel.currentQuestionText
        // Log a message at DEBUG log level
        Log.d(TAG, "Current question index: $questionTextResID")

        try {
            val question = quizViewModel.currentQuestionAnswer
        } catch (ex: ArrayIndexOutOfBoundsException) {
            // Log a message at ERROR log level, along with an exception stack trace
            Log.e(TAG, "Index was out of bounds", ex)
        }
        questionTextView.setText(questionTextResID)

        if(quizViewModel.currentIndex == quizViewModel.bankSize-1){
            nextButton.isEnabled = false
            nextButton.isClickable = false
            prevButton.isClickable = false
            prevButton.isEnabled = false
        }
    }

    private fun checkAnswer(userAnswer: Boolean){
        quizViewModel.enableButton = false
        enableTrueFalseButton()
        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResID = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

        Toast.makeText(this, messageResID, Toast.LENGTH_SHORT).show()
        scoreUpdate(userAnswer, correctAnswer, quizViewModel.isCheater)

    }

    private fun enableTrueFalseButton(){
        val status = quizViewModel.enableButton
        trueButton.isEnabled = status
        trueButton.isClickable = status
        falseButton.isEnabled = status
        falseButton.isClickable = status
    }


    private fun scoreUpdate(userAnswer: Boolean, correctAnswer:Boolean, cheater: Boolean){
        var score = quizViewModel.points
        if(userAnswer == correctAnswer && !cheater) {
            quizViewModel.updateGrade(true)
        }else if (cheater){
            quizViewModel.updateGrade(false)
        }
        score = quizViewModel.points


        if(quizViewModel.currentIndex == quizViewModel.bankSize-1) {
           var grade = finalGrade(score)
            Toast.makeText(this, "Final score $score /6. Quiz Grade $grade", Toast.LENGTH_SHORT).show()
            nextButton.isEnabled =true
            nextButton.isClickable =true
            nextButton.setText("Reset")
            quizViewModel.points = 0
        }else{
            Toast.makeText(this, "Current score $score /6", Toast.LENGTH_SHORT).show()
        }
    }

    private fun finalGrade(score: Int): String {
        var grade = "A"
        if(score == 5){
            grade = "B"
        }else if( score == 4 ||  score == 3){
            grade = "C"
        }else if(score == 2 ){
            grade = "D"
        }else if(score == 1 || score == 0){
            grade = "F"
        }
        return grade
    }

    private fun enablePreviousButton(){
        if(quizViewModel.currentIndex == 0){
            prevButton.isEnabled = false
            prevButton.isClickable = false
        }else{
            prevButton.isEnabled = true
            prevButton.isClickable = true
        }
    }
}

