package com.bignerdranch.android.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {

    private val questionBank = listOf(
        Question(R.string.question_US, true),
        Question(R.string.question_Luke, false),
        Question(R.string.question_Creator, true),
        Question(R.string.question_Obiwan, false),
        Question(R.string.question_Solo, false),
        Question(R.string.question_Movie, true)
    )

     var currentIndex = 0
    var isCheater = false
    var bankSize = questionBank.size
    var points = 0
    var enableButton = true



    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResID

    fun moveToNext(){
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun updateGrade(correct :Boolean){
        if(correct){
            points += 1
        }else if(points != 0 && !correct){
            points -= 1
        }
    }

    fun previousQuestion(): Boolean {
        var back = false
        if(currentIndex != 0) {
            currentIndex = (currentIndex - 1) % questionBank.size
            back = true
        }
        return back
    }

}