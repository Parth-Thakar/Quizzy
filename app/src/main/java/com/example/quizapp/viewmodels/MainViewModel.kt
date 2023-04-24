package com.example.quizapp.viewmodels

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.model.Question
import com.example.quizapp.repository.QuestionsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

    private const val COUNTDOWN_TIMER = 600000L
    private const val ONE_SECOND = 1000L
    private const val DONE = 0L

class MainViewModel(private val repository: QuestionsRepository) : ViewModel() {

    lateinit var timer: CountDownTimer
    var currentTime = MutableLiveData<Long>()
    val totalScore = MutableLiveData(0)

    // live data using getters of repository
    val questions: MutableLiveData<List<Question>>
        get() = repository.questions

    init {
        // coroutine scope of viewModelScope.launch using dispatcher.IO to get the contributor list from the API
        viewModelScope.launch(Dispatchers.IO) {
            repository.getContributor()
        }

    }
    // Start Timer Function
    fun timerStart() {
        // LOGIC FOR THE TIMER IMPLEMENTATION ON MAINACTIVITY
        timer = object : CountDownTimer(COUNTDOWN_TIMER, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                currentTime.value = millisUntilFinished / ONE_SECOND
            }
            override fun onFinish() {
                currentTime.value = DONE
            }
        }
        timer.start()
    }
    // Stop Timer Function
    fun stopTimer() {
        timer.cancel()
    }
}