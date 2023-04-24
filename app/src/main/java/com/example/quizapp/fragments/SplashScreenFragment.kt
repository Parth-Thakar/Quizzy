package com.example.quizapp.fragments

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.quizapp.MainActivity
import com.example.quizapp.R

class SplashScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_splash_screen, container, false)

        //for 1.5 sec
        val time: Long = 1500

        Handler().postDelayed({
            if (savedInstanceState == null) {
                activity?.supportFragmentManager?.beginTransaction()?.replace(
                    R.id.fragmentSpace,
                    SetupScreenFragment()
                )?.commit()
            }
        }, time)

        return view

    }


}