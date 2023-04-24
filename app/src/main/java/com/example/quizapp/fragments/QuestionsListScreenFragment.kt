package com.example.quizapp.fragments

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.R
import com.example.quizapp.adapter.QuestionListRecyclerViewAdapter
import com.example.quizapp.model.Question
import com.example.quizapp.model.QuestionList
import com.example.quizapp.viewmodels.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class QuestionsListScreenFragment : Fragment(), QuestionListRecyclerViewAdapter.onItemClickListner {

    private val mainviewmodel: MainViewModel by activityViewModels()
    lateinit var timerTextView: TextView
    lateinit var listToSend: List<Question>
    var totalScore = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_questions_list_screen, container, false)

        // connect the widget
        val submitButton: Button = view.findViewById(R.id.submitButton)
        timerTextView = view.findViewById(R.id.timerTextview)

        // Raising the Alert Dialog and opening the summary Screen
        submitButton.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.alert_title))
                .setMessage(getString(R.string.alert_message))
                .setPositiveButton(getString(R.string.alert_positive_button)) { dialog, which ->
                    endQuiz()
                }
                .setNegativeButton(getString(R.string.no_negative_text)) { dialog, which ->
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.alert_negative_button),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .show()
        }

        // recyclerview connection
        val questionListRecyclerView: RecyclerView =
            view.findViewById(R.id.questionListRecyclerView)
        val layoutManager = LinearLayoutManager(activity)
        questionListRecyclerView.layoutManager = layoutManager


        // fetching the data from live data using observe function.
        activity?.let {
            mainviewmodel.questions.observe(it) {
                listToSend = it
                questionListRecyclerView.adapter = QuestionListRecyclerViewAdapter(listToSend,this)
            }
        }
        // setting up the Timer using viewModel
        mainviewmodel.currentTime.observe(viewLifecycleOwner) {
            if (it != 0L) {
                timerTextView.text = DateUtils.formatElapsedTime(it)
            } else {
                endQuiz()
            }
        }

        return view
    }




    private fun endQuiz() {
        // calculating the quiz
        for (it in listToSend) {
            if (it.correctStatus) {
                totalScore++
            }
        }
        // posting total Score to LiveData in view model instance
        mainviewmodel.totalScore.postValue(totalScore)
        // reintializing the local score varaible to zero.
        totalScore = 0
        // Loading the Summary Screen Fragment.
        val summaryScreenFragment = SummaryScreenFragment()
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fragmentSpace, summaryScreenFragment)
            ?.commit()
        mainviewmodel.stopTimer()
    }


    override fun onItemClick(position: Int, dataList: List<Question>) {
        val questionDetailScreenFragment = QuestionDetailScreenFragment()

        val bundle = Bundle()
        val questionsToSend = QuestionList(dataList)
        bundle.putSerializable(getString(R.string.bundle_key), questionsToSend)
        bundle.putInt(getString(R.string.position_index_key), position)
        questionDetailScreenFragment.arguments = bundle

        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fragmentSpace, questionDetailScreenFragment,getString(R.string.details_screen_fragment_tag))?.addToBackStack(null)
            ?.commit()
    }


}


