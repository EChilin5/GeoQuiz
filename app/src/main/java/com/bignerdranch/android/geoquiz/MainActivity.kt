package com.bignerdranch.android.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast


// determine the source of the package
private const val TAG = "MAINACTIVITY"

class MainActivity : AppCompatActivity() {
    // stopped on this section
    // https://learning.oreilly.com/library/view/android-programming-the/9780135257555/ch03s05.html


    // lateinit is used to indicate to teh compiler that it is a non-null view
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var prevButton:Button

    private val questionBank = listOf(
            Question(R.string.question_australia, true),
            Question(R.string.question_oceans, true),
            Question(R.string.question_mideast, false),
            Question(R.string.question_africa, false),
            Question(R.string.question_americas, true),
            Question(R.string.question_asia, true)
    )

    private var currentIndex = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView =findViewById(R.id.question_text_view)
        prevButton = findViewById(R.id.previous_button)

        trueButton.setOnClickListener { view: View ->
           checkAnswer(true)
        }

        falseButton.setOnClickListener{ view:View->
          checkAnswer(false)
        }

        nextButton.setOnClickListener{view:View->
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        questionTextView.setOnClickListener{view:View->
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        prevButton.setOnClickListener{view:View->
            if(currentIndex != 0){
                currentIndex = (currentIndex - 1) % questionBank.size
                updateQuestion()
            }else{
                Toast.makeText(this, "Can not go back", Toast.LENGTH_SHORT).show()
            }

        }

        updateQuestion()
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

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "OnDestroy() called")
    }


    private fun updateQuestion(){
        val questionTextResID = questionBank[currentIndex].textResID
        // Log a message at DEBUG log level
        Log.d(TAG, "Current question index: $currentIndex")

        try {
            val question = questionBank[currentIndex]
        } catch (ex: ArrayIndexOutOfBoundsException) {
            // Log a message at ERROR log level, along with an exception stack trace
            Log.e(TAG, "Index was out of bounds", ex)
        }
        questionTextView.setText(questionTextResID)
    }

    private fun checkAnswer(userAnswer: Boolean){
        val correctAnswer = questionBank[currentIndex].answer

        val messageResID = if(userAnswer == correctAnswer){
            R.string.correct_toast
        }else{
            R.string.incorrect_toast
        }

        Toast.makeText(this, messageResID, Toast.LENGTH_SHORT).show()

    }
}