package com.example.groupproject

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.groupproject.databinding.ActivityQuizBinding
import com.example.groupproject.databinding.ScoreDialogBinding


class QuizActivity : AppCompatActivity(), View.OnClickListener {

    // Companion object to hold shared properties
    companion object {
        var questionModelList: List<QuestionModel> = listOf() // List of QuestionModel objects
        var time: String = "" // String to hold time
    }

    // Late-initialized binding variable
    lateinit var binding: ActivityQuizBinding

    // Variables for quiz control
    var currentQuestionIndex = 0
    var selectedAnswer = ""
    var score = 0

    // Override method to initialize the activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflating layout using view binding
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setting onClickListeners for buttons
        binding.apply {
            btn0.setOnClickListener(this@QuizActivity)
            btn1.setOnClickListener(this@QuizActivity)
            btn2.setOnClickListener(this@QuizActivity)
            btn3.setOnClickListener(this@QuizActivity)
            nextBtn.setOnClickListener(this@QuizActivity)
            prevBtn.setOnClickListener(this@QuizActivity)
        }

        // Load questions and start timer
        loadQuestions()
        startTimer()
    }

    // Method to start the timer for the quiz
    private fun startTimer() {
        val totalTimeInMillis = time.toInt() * 60 * 1000L
        object : CountDownTimer(totalTimeInMillis, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                val minutes = seconds / 60
                val remainingSeconds = seconds % 60
                binding.timerIndicatorTextview.text = String.format("%02d:%02d", minutes, remainingSeconds)
            }

            override fun onFinish() {
                // Finish the quiz
            }
        }.start()
    }

    // Method to load questions into the quiz layout
    private fun loadQuestions() {
        selectedAnswer = ""
        if (currentQuestionIndex == questionModelList.size) {
            finishQuiz()
            return
        }

        // Setting up question and answer options
        binding.apply {
            questionIndicatorTextview.text = "Question ${currentQuestionIndex + 1}/ ${questionModelList.size} "
            questionProgressIndicator.progress =
                (currentQuestionIndex.toFloat() / questionModelList.size.toFloat() * 100).toInt()
            questionTextview.text = questionModelList[currentQuestionIndex].question
            btn0.text = questionModelList[currentQuestionIndex].options[0]
            btn1.text = questionModelList[currentQuestionIndex].options[1]
            btn2.text = questionModelList[currentQuestionIndex].options[2]
            btn3.text = questionModelList[currentQuestionIndex].options[3]
        }
    }

    // Method to handle button clicks
    override fun onClick(view: View?) {
        binding.apply {
            // Resetting button colors
            btn0.setBackgroundColor(getColor(R.color.gray))
            btn1.setBackgroundColor(getColor(R.color.gray))
            btn2.setBackgroundColor(getColor(R.color.gray))
            btn3.setBackgroundColor(getColor(R.color.gray))
        }

        val clickedBtn = view as Button
        if (clickedBtn.id == R.id.next_btn) {
            // Next button is clicked
            if (selectedAnswer.isEmpty()) {
                Toast.makeText(applicationContext, "Please select an answer to continue", Toast.LENGTH_SHORT).show()
                return
            }
            if (selectedAnswer == questionModelList[currentQuestionIndex].correct) {
                score++
                Log.i("Score of quiz", score.toString())
            }
            currentQuestionIndex++
            loadQuestions()
        } else if (clickedBtn.id == R.id.prev_btn) {
            // Previous button is clicked
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--
                loadQuestions()
            } else {
                Toast.makeText(applicationContext, "This is the first question", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Options button is clicked
            selectedAnswer = clickedBtn.text.toString()
            clickedBtn.setBackgroundColor(getColor(R.color.orange))
        }
    }

    // Method to finish the quiz
    private fun finishQuiz() {
        val totalQuestions = questionModelList.size
        val percentage = ((score.toFloat() / totalQuestions.toFloat()) * 100).toInt()

        // Inflate and set up dialog for displaying score
        val dialogBinding = ScoreDialogBinding.inflate(layoutInflater)
        dialogBinding.apply {
            scoreProgressIndicator.progress = percentage
            scoreProgressText.text = "$percentage %"
            if (percentage > 60) {
                scoreTitle.text = "Congrats! You have passed"
                scoreTitle.setTextColor(Color.BLUE)
            } else {
                scoreTitle.text = "Oops! You have failed"
                scoreTitle.setTextColor(Color.RED)
            }
            scoreSubtitle.text = "$score out of $totalQuestions are correct"
            finishBtn.setOnClickListener {
                finish()
            }
        }

        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .show()

    }
}
