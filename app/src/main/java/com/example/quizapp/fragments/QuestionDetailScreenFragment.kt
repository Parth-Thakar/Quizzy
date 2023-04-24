package com.example.quizapp.fragments


import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.quizapp.R
import com.example.quizapp.model.Question
import com.example.quizapp.model.QuestionList
import com.example.quizapp.viewmodels.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class QuestionDetailScreenFragment : Fragment() {

    lateinit var questionTextView: TextView
    lateinit var questionNumberTextView: TextView
    lateinit var optionButton1: RadioButton
    lateinit var optionButton2: RadioButton
    lateinit var optionButton3: RadioButton
    lateinit var optionButton4: RadioButton
    lateinit var radioGroup: RadioGroup

    lateinit var previousButton: Button
    lateinit var nextButton: Button
    lateinit var selectOptionButton: Button
    lateinit var bookMarkStatusButton: ImageView
    lateinit var timerTextView: TextView
    lateinit var submitButton: Button

    var correctOptionString = ""
    var totalScore = 0
    var indexPosition = 0

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var questions: QuestionList

    lateinit var listToSend: List<Question>

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_question_detail_screen, container, false)


        // connect the widgets
        questionTextView = view.findViewById(R.id.questionTextView)
        questionNumberTextView = view.findViewById(R.id.questionNumberTextView)
        optionButton1 = view.findViewById(R.id.optionButton1)
        optionButton2 = view.findViewById(R.id.optionButton2)
        optionButton3 = view.findViewById(R.id.optionButton3)
        optionButton4 = view.findViewById(R.id.optionButton4)

        previousButton = view.findViewById(R.id.previousButton)
        nextButton = view.findViewById(R.id.nextButton)

        radioGroup = view.findViewById(R.id.optionRadioGroup)
        selectOptionButton = view.findViewById(R.id.selectOptionButton)
        bookMarkStatusButton = view.findViewById(R.id.bookMarkStatusButton)

        timerTextView = view.findViewById(R.id.timerTextViewDetailScreen)
        submitButton = view.findViewById(R.id.submitButton)


        // GETTING THE ARGUMENTS USING BUNDLE
        val args = this.arguments
        // TYPECASTING IT TO DATA CLASS
        questions = args?.get(getString(R.string.bundle_key)) as QuestionList
        indexPosition = args?.get(getString(R.string.position_index_key)) as Int


        //set Question to fragment.
        setQuestion(questions, indexPosition)

        // Previous Button Clicked
        previousButton.setOnClickListener {
            radioGroup.clearCheck()
            if (indexPosition != 0) {
                indexPosition--
                setQuestion(questions, indexPosition)
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.first_question_list_warning_toast),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        // Previous Button Clicked
        nextButton.setOnClickListener {
            radioGroup.clearCheck()
            if (indexPosition != questions.questions.size - 1) {
                indexPosition++
                setQuestion(questions, indexPosition)
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.last_question_list_warning_toast),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        // Selected Option Button Clicked
        selectOptionButton.setOnClickListener {
            validateSolution()

        }
        //Bookmark status Icon on click
        bookMarkStatusButton.setOnClickListener {
            questions.questions[indexPosition].bookMarkStatus = true
            mainViewModel?.questions?.postValue(questions.questions)
            bookMarkStatusButton.setColorFilter(Color.YELLOW)
        }

        // setting up the Timer using viewModel
        mainViewModel.currentTime.observe(viewLifecycleOwner) {
            if (it != 0L) {
                timerTextView.text = DateUtils.formatElapsedTime(it)
            } else {
                endQuiz()
            }
        }


        // fetching the data from live data using observe function.
        activity?.let {
            mainViewModel.questions.observe(it) {
                listToSend = it
            }
        }


        submitButton.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.alert_title))
                .setMessage(getString(R.string.alert_message))
                .setPositiveButton(getString(R.string.alert_positive_button)) { dialog, which ->
                    endQuiz()
                }
                .setNegativeButton(getString(R.string.no_alert_button_message)) { dialog, which ->
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.alert_negative_button),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .show()
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
        mainViewModel.totalScore.postValue(totalScore)
        // reintializing the local score varaible to zero.
        totalScore = 0
        var sharedPreferences: SharedPreferences? =
            this.activity?.getSharedPreferences(SetupScreenFragment::PREFS_NAME.toString(), 0)
        // declaring the editor object to edit the boolean data
        var editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        // changing the boolean data of key hasLoggedIn to true because credentials is matched now
        editor?.putBoolean(getString(R.string.hasstarted_prefs_key), false)
        editor?.commit()

        // Loading the Summary Screen Fragment.
        val summaryScreenFragment = SummaryScreenFragment()

        activity?.supportFragmentManager?.popBackStack()

        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fragmentSpace, summaryScreenFragment)
            ?.commit()
        mainViewModel.stopTimer()
    }

    private fun validateSolution() {
        val idOfCheckedRadio = radioGroup.checkedRadioButtonId
        val check = view?.findViewById<RadioButton>(idOfCheckedRadio)
        if (check == null) {
            Toast.makeText(
                requireContext(),
                getString(R.string.no_option_selected),
                Toast.LENGTH_SHORT
            ).show()
        }
        else {
            questions.questions[indexPosition].selectedOptionString = check?.text.toString()
            lockTheOption(questions.questions[indexPosition])
            val checkText = check?.text
            if (checkText == correctOptionString) {
                questions.questions[indexPosition].correctStatus = true
            }
            questions.questions[indexPosition].status = true
            mainViewModel?.questions?.postValue(questions.questions)
            mainViewModel?.totalScore?.postValue(totalScore)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setQuestion(questions: QuestionList, indexPosition: Int) {
        val questionDetails = questions.questions[indexPosition]

        val question = questionDetails.question
        val options = questionDetails.options
        val correctOptionNumber = questionDetails.correct_option
        val bookMarkStatus = questionDetails.bookMarkStatus

        if (bookMarkStatus == true) {
            bookMarkStatusButton.setColorFilter(Color.YELLOW)
        } else {
            bookMarkStatusButton.setColorFilter(Color.WHITE)
        }
        correctOptionString = options[correctOptionNumber]

        questionTextView.text = question
        val questionNumber = indexPosition + 1
        questionNumberTextView.text = "Q$questionNumber :"

        val shuffledOptionList = options.shuffled()

        optionButton1.text = shuffledOptionList[0]
        optionButton2.text = shuffledOptionList[1]
        optionButton3.text = shuffledOptionList[2]
        optionButton4.text = shuffledOptionList[3]

        if (questionDetails.status) {
            lockTheOption(questionDetails)
        } else {
            optionButton1.isEnabled = true
            optionButton2.isEnabled = true
            optionButton3.isEnabled = true
            optionButton4.isEnabled = true

            selectOptionButton.text = getString(R.string.lock_the_option)
            selectOptionButton.isEnabled = true
        }


    }

    private fun lockTheOption(questionDetails: Question) {
        optionButton1.isEnabled = false
        optionButton2.isEnabled = false
        optionButton3.isEnabled = false
        optionButton4.isEnabled = false

        selectOptionButton.text = getString(R.string.already_answered_text)
        selectOptionButton.isEnabled = false
        val selectedOption = questionDetails.selectedOptionString


        if (optionButton1.text.toString().equals(selectedOption)) {
            optionButton1.isChecked = true
        }
        if (optionButton2.text.toString().equals(selectedOption)) {
            optionButton2.isChecked = true
        }
        if (optionButton3.text.toString().equals(selectedOption)) {
            optionButton3.isChecked = true
        }
        if (optionButton4.text.toString().equals(selectedOption)) {
            optionButton4.isChecked = true
        }

    }


}