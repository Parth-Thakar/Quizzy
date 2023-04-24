package com.example.quizapp.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.example.quizapp.MainActivity
import com.example.quizapp.R
import com.example.quizapp.viewmodels.MainViewModel

const val COUNTDOWN_TIMER = 600000L

class SummaryScreenFragment : Fragment() {

    lateinit var scoreTextView: TextView
    lateinit var timeTakenTextView: TextView
    lateinit var restartQuiz: Button
    lateinit var exitQuiz: Button
    lateinit var shareButton: ImageView
    private val mainviewmodel: MainViewModel by activityViewModels()
    var score = 0

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_summary_screen, container, false)

        scoreTextView = view.findViewById(R.id.scoreTextView)
        restartQuiz = view.findViewById(R.id.restartButton)
        exitQuiz = view.findViewById(R.id.exitButton)
        timeTakenTextView = view.findViewById(R.id.timeTakenTextView)
        shareButton = view.findViewById(R.id.shareButton)

        // showing the marks score of the user to the screen
        score = mainviewmodel.totalScore.value!!
        scoreTextView.text = getString(R.string.your_score_template) + score.toString()

        // calculating the time taken by the user to complete the quiz
        val timeRemaining = mainviewmodel.currentTime.value
        val timeTake = (COUNTDOWN_TIMER / 1000L) - timeRemaining!!


        timeTakenTextView.text =
            getString(R.string.time_taken_template) + timeTake.let { DateUtils.formatElapsedTime(it) }

        // on Exit Button Presed
        exitQuiz.setOnClickListener {
            activity?.finish()
            System.exit(0)
        }
        // restarting the quiz
        restartQuiz.setOnClickListener {
            val questions = mainviewmodel.questions.value
            if (questions != null) {
                for (it in questions) {
                    it.status = false
                    it.bookMarkStatus = false
                    it.correctStatus = false
                }
            }
            mainviewmodel.questions.postValue(questions)
            val setupScreenFragment = SetupScreenFragment()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentSpace, setupScreenFragment)
                ?.commit()
            mainviewmodel.timerStart()
        }

        //share button logic
        shareButton.setOnClickListener {
            val intent: Intent = Intent(Intent.ACTION_SEND)
            intent.setType(getString(R.string.text_plain_type))
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject))
            intent.putExtra(Intent.EXTRA_TEXT, buildString {
                append(getString(R.string.score_text))
                append(score)
                append(getString(R.string.score_concatenate_message))
                append(timeTake.let { DateUtils.formatElapsedTime(it) })
                append(" ")
            })
            startActivity(Intent.createChooser(intent, getString(R.string.share_via_button)))

        }

        return view
    }
}