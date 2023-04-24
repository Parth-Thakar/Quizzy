package com.example.quizapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quizapp.repository.QuestionsRepository

class MainViewModelFactory(private val repository: QuestionsRepository) :
    ViewModelProvider.Factory {
    // MAINVIEWMODEL FACTORY THAT WAS RECOMMENDED FOR MULTIPLE INSTANCW OF THE MAINVIEWMODEL
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return MainViewModel(repository) as T
    }

}