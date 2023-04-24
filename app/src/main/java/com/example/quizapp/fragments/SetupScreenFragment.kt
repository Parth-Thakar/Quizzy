package com.example.quizapp.fragments

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.R
import com.example.quizapp.adapter.RulesRecyclerViewAdapter
import com.example.quizapp.viewmodels.MainViewModel

class SetupScreenFragment : Fragment() {

    open val PREFS_NAME: String = activity?.getString(R.string.shared_prefs_file).toString()
    private val mainviewmodel: MainViewModel by activityViewModels()

    lateinit var rulesRecyclerView : RecyclerView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_setup_screen, container, false)

        val startQuizButton: Button = view.findViewById(R.id.startQuizButton)


        startQuizButton.setOnClickListener {
            //starting the timer in mainviewmodel
            mainviewmodel.timerStart()
            // loading the Question List Fragment
            val questionsListScreenFragment = QuestionsListScreenFragment()
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(
                    R.id.fragmentSpace,
                    questionsListScreenFragment,
                    activity?.getString(R.string.question_list_fragment_tag)
                )
                ?.commit()
            // Changing the Prefrence value to Game Started
            var sharedPreferences: SharedPreferences? =
                this.activity?.getSharedPreferences(SetupScreenFragment::PREFS_NAME.toString(), 0)

            // declaring the editor object to edit the booleand data
            var editor: SharedPreferences.Editor? = sharedPreferences?.edit()

            // changing the boolean data of key hasLoggedIn to true because credentials is matched now
            editor?.putBoolean(activity?.getString(R.string.hasstarted_prefs_key), true)
            editor?.commit()
        }

        rulesRecyclerView = view.findViewById(R.id.rulesRecyclerView)

        val layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        rulesRecyclerView.layoutManager = layoutManager

        val rulesList  = ArrayList<String>()

        rulesList.add(getString(R.string.rule_1))
        rulesList.add(getString(R.string.rule_2))
        rulesList.add(getString(R.string.rule_3))
        rulesList.add(getString(R.string.rule_4))
        rulesList.add(getString(R.string.rule_5))

        rulesRecyclerView.adapter = RulesRecyclerViewAdapter(rulesList)

        return view
    }


}