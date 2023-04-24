package com.example.quizapp.model

import java.io.Serializable

data class Question(
    val correct_option: Int,
    val id: Int,
    val options: List<String>,
    val question: String,
    var status : Boolean = false,
    var bookMarkStatus : Boolean = false,
    var correctStatus : Boolean = false,
    var selectedOptionString : String
) : Serializable