package com.example.quizapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    // making the retrofit builder instance for the base url
    private const val BASE_URL =
        "https://raw.githubusercontent.com/tVishal96/sample-english-mcqs/master/"

    // RETROFIT OBJECT DECLARATION FUNCTION WITH BASE URL
    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}