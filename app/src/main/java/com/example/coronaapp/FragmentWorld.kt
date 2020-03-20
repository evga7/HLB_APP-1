package com.example.coronaapp

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_world.*
import org.jsoup.Jsoup
import java.io.IOException
import java.time.LocalDate

class FragmentWorld : Fragment() {
    val url = "https://www.worldometers.info/coronavirus/"
    var coronaList = ArrayList<Information>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        try {
//            coronaList = WorldCrawling().execute(url).get()
//        }catch (e : IOException) {
//            e.printStackTrace();
//        }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_world, container, false)
        //a.worldtext.text = coronaList.toString()

        val worldRecyclerView = rootView.findViewById(R.id.worldrecyclerview) as RecyclerView
        worldRecyclerView.layoutManager = LinearLayoutManager(activity)
        worldRecyclerView.adapter = WorldAdapter(coronaList)

        return rootView
        //return inflater.inflate(R.layout.fragment_world, container, false)

    }

}

class WorldCrawling : AsyncTask<String, String, ArrayList<Information>>() { // 세번째 doinbackground 반환타입, onPostExecute 매개변수
    var infoList: ArrayList<Information> = arrayListOf()

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg params: String?): ArrayList<Information> {

        try{
            Log.d(params[0].toString(),"왜안되는고야")

            val doc = Jsoup.connect(params[0]).get()
            //val data = doc.select("#main_table_countries > tbody > tr")
            val data = doc.select("#main_table_countries_yesterday > tbody > tr")
            Log.d(data.toString(),"모")
            var country :String
            var totalCases : String
            var newCases : String
            var totalDeaths : String
            var newDeaths : String
            var totalRecovered : String

            // 임시 cnt
            var cnt :Int = 0

            for (datum in data){
                country = datum.select("td")[0].text().trim()
                totalCases = datum.select("td")[1].text().trim()
                newCases = datum.select("td")[2].text().trim()
                totalDeaths = datum.select("td")[3].text().trim()
                newDeaths = datum.select("td")[4].text().trim()
                totalRecovered = datum.select("td")[5].text().trim()

                var total = Information(country,totalCases,newCases,newDeaths,totalDeaths,totalRecovered)
                infoList.add(total)

                cnt++

                if (cnt == 10){
                    break
                }
            }

        }catch (e : IOException) {
            Log.d("안됨","안딤")
            e.printStackTrace()
        }
        return infoList
    }

    override fun onProgressUpdate(vararg values: String?) { // UI 업데이
        super.onProgressUpdate(*values)
    }

    override fun onPostExecute(result: ArrayList<Information>) {
        super.onPostExecute(result)
    }

}

//data class Information(val country:String, val totalCases:String, val newCases : String, val totalDeaths : String, val newDeaths : String, val totalRecovered : String)


