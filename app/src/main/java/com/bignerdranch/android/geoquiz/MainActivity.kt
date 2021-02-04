package com.bignerdranch.android.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

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

    private fun updateQuestion(){
        val questionTextResID = questionBank[currentIndex].textResID
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