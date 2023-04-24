package com.example.quizapp.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.R
import com.example.quizapp.fragments.QuestionDetailScreenFragment
import com.example.quizapp.fragments.QuestionsListScreenFragment
import com.example.quizapp.model.Question
import com.example.quizapp.model.QuestionList

class QuestionListRecyclerViewAdapter(
    private val dataList: List<Question>,
    private val listner : onItemClickListner
    ) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view =
            inflater.inflate(R.layout.item_questions_of_recyclerview, parent, false)
        return QuestionListViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val questionName = dataList[position].question
        val questionNumber = position + 1
        var answerStatus = dataList[position].status
        if (answerStatus == null) {
            answerStatus = false
        }
        val bookMarkStatus = dataList[position].bookMarkStatus

        (holder as QuestionListViewHolder).bind(
            "Q$questionNumber - $questionName",
            answerStatus,
            bookMarkStatus
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    // viewholder class bind function will bind the list data to recyclerview item's textview
    inner class QuestionListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init{
            itemView.setOnClickListener(this)
        }

        val questionName: TextView =
            itemView.findViewById(R.id.questionName)

        val answerStatusImage: ImageView = itemView.findViewById(R.id.answerStatus)
        val bookMarStatusImage: ImageView = itemView.findViewById(R.id.bookMarkIcon)


        fun bind(stringToSet: String, answerStatus: Boolean, bookMarkStatus: Boolean) {
            questionName.text = stringToSet
            if (answerStatus == true) {
                answerStatusImage.visibility = View.VISIBLE
            } else {
                answerStatusImage.visibility = View.GONE
            }
            if (bookMarkStatus) {
                bookMarStatusImage.setColorFilter(Color.YELLOW)
            } else {
                bookMarStatusImage.setColorFilter(Color.WHITE)
            }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listner.onItemClick(position, dataList)
            }
        }
    }

    interface onItemClickListner{
        fun onItemClick(position: Int,dataList: List<Question>)
    }

}