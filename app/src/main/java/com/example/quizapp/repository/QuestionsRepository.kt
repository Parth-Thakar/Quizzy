package com.example.quizapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.quizapp.api.QuestionsApi
import com.example.quizapp.model.Question
import com.example.quizapp.model.QuestionList

class QuestionsRepository(private val questionsApi: QuestionsApi) {

    private val questionLiveData = MutableLiveData<List<Question>>()

    // creating the Live Data for the viewmodel
    val questions: MutableLiveData<List<Question>>
        get() = questionLiveData

    suspend fun getContributor() {
        val result = questionsApi.getQuestions()
        val shuffeledResult = result.body()?.questions?.shuffled()
        if (result?.body() != null) {
            questionLiveData.postValue(shuffeledResult!!)
        }
    }

}