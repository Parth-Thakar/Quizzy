package com.example.quizapp.api

import com.example.quizapp.model.QuestionList
import retrofit2.Response
import retrofit2.http.GET

interface QuestionsApi {

    // Get request with Api end point and suspend function as asked to use the coroutines
    @GET("db.json")
    suspend fun getQuestions(): Response<QuestionList>

}