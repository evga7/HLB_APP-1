package com.example.coronaapp

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.world_list.view.*

class WorldAdapter(val worldItems:ArrayList<Information>) : RecyclerView.Adapter<WorldAdapter.WorldViewHolder>() {

    var items: MutableList<Information> = mutableListOf(
        Information("국가","확진자","사망자","회복"),
        Information("china","80,928"+"\n"+"+34","3,245"+"\n"+"+34","70,420")
        ,Information("요맨","111"+"\n"+"+34","300"+"\n"+"+34","100"),
        Information("china","80,928"+"\n"+"+34","3,245"+"\n"+"+34","70,420")
        ,Information("요맨","111"+"\n"+"+34","300"+"\n"+"+34","100"),
        Information("china","80,928"+"\n"+"+34","3,245"+"\n"+"+34","70,420")
        ,Information("요맨","111"+"\n"+"+34","300"+"\n"+"+34","100"),
        Information("china","80,928"+"\n"+"+34","3,245"+"\n"+"+34","70,420")
        ,Information("요맨","111"+"\n"+"+34","300"+"\n"+"+34","100")
    )

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WorldViewHolder(parent)

    //items -> textview
    override fun onBindViewHolder(holder: WorldViewHolder, pos: Int) {
        val recyclerviewColor = arrayListOf<String>("#B1BCBE")

        if (pos == 0){ // Title Textview 속성 값
            val textParam = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT).apply {
                topMargin = 0
                bottomMargin = 0
                leftMargin = 0
                rightMargin = 0
                width = 0
                weight = 1.0f
                gravity = Gravity.CENTER_HORIZONTAL
            }

            holder.bind(items[pos])
            holder.country.layoutParams = textParam
            holder.totalCases.layoutParams = textParam
            holder.totalDeaths.layoutParams = textParam
            holder.totalRecovered.layoutParams = textParam
            holder.itemView.setBackgroundColor(Color.parseColor(recyclerviewColor[0]))

        }
        else{
            //holder.itemView.setBackgroundColor(Color.parseColor(recyclerviewColor[0]))
            holder.bind(items[pos])

            // + 부분부터 색깔 변환
            val totalCasesPlus = holder.totalCases.text.indexOf('+')
            val totalDeathsPlus = holder.totalDeaths.text.indexOf('+')

            val spannableCases = SpannableString(holder.totalCases.text)
            val spannableDeaths = SpannableString(holder.totalDeaths.text)

            spannableCases.setSpan(ForegroundColorSpan(Color.RED), totalCasesPlus, holder.totalCases.text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableDeaths.setSpan(ForegroundColorSpan(Color.RED), totalDeathsPlus, holder.totalDeaths.text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            holder.totalCases.setText(spannableCases, TextView.BufferType.SPANNABLE)
            holder.totalDeaths.setText(spannableDeaths, TextView.BufferType.SPANNABLE)

        }
    }


    inner class WorldViewHolder(parent: ViewGroup?) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent?.context).inflate(R.layout.world_list, parent, false)) {
        val country = itemView.country
        val totalCases= itemView.totalCases
        val totalDeaths= itemView.totalDeaths
        val totalRecovered = itemView.totalRecovered

        fun bind(listInfo:Information){
            country?.text = listInfo.country
            totalCases?.text = listInfo.totalCases
            totalDeaths?.text = listInfo.totalDeaths
            totalRecovered?.text = listInfo.totalRecovered
        }

    }

}