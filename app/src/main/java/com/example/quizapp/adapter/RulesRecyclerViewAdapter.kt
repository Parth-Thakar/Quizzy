package com.example.quizapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.R

class RulesRecyclerViewAdapter(
    private val dataList: ArrayList<String>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view =
            inflater.inflate(R.layout.item_of_rues_recyclerview, parent, false)
        return RulesListViewHolder(view)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        (holder as RulesRecyclerViewAdapter.RulesListViewHolder).bind(
            dataList[position],
            position
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    class RulesListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rulesText: TextView = itemView.findViewById(R.id.rulesText)
        val rulesImage: ImageView = itemView.findViewById(R.id.rulesRelatedIcon)

        fun bind(stringToSet: String, position: Int) {
            rulesText.text = stringToSet
            when (position) {
                0 -> rulesImage.setImageResource(R.drawable.ic_baseline_timer_24)
                1 -> rulesImage.setImageResource(R.drawable.question)
                2 -> rulesImage.setImageResource(R.drawable.edit)
                3 -> rulesImage.setImageResource(R.drawable.lock)
                4 -> rulesImage.setImageResource(R.drawable.lock)
                else -> {
                    rulesImage.setImageResource(R.drawable.ic_baseline_timer_24)
                }
            }

        }
    }

}

