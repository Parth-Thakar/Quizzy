package com.example.quizapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.quizapp.api.QuestionsApi
import com.example.quizapp.api.RetrofitHelper
import com.example.quizapp.fragments.QuestionsListScreenFragment
import com.example.quizapp.fragments.SetupScreenFragment
import com.example.quizapp.fragments.SplashScreenFragment
import com.example.quizapp.repository.QuestionsRepository
import com.example.quizapp.viewmodels.MainViewModel
import com.example.quizapp.viewmodels.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var setupScreenFragment: SetupScreenFragment
    private lateinit var questionsListScreenFragment: QuestionsListScreenFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val questionApi = RetrofitHelper.getInstance().create(QuestionsApi::class.java)

        val repository = QuestionsRepository(questionApi)

        val mainviewmodel = ViewModelProvider(
            this,
            MainViewModelFactory(repository)
        ).get(MainViewModel::class.java)


        if (savedInstanceState == null) {
            val splashScreenFragment = SplashScreenFragment()
            val fragmentManager: FragmentManager = supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(
                R.id.fragmentSpace,
                splashScreenFragment,
                getString(R.string.splash_fragment_tag)
            ).commit()
        }

    }


}