package com.HLB.coronamagnifier.world.worldAsync

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.HLB.coronamagnifier.progresscircle.CustomProgressCircle
import com.HLB.coronamagnifier.R
import com.HLB.coronamagnifier.singleton.Singleton
import com.HLB.coronamagnifier.world.worldData.CountryTrans
import com.HLB.coronamagnifier.world.worldData.Information
import org.jsoup.Jsoup
import java.io.IOException
import kotlin.collections.ArrayList

class WorldCrawling(act:AppCompatActivity, context: Context,frg:Fragment) : AsyncTask<String, String, ArrayList<Information>>() {
    var infoList: ArrayList<Information> = arrayListOf()
    val progressCircle = CustomProgressCircle()
    val dialogContext : Context = context
    val currentActivity:AppCompatActivity = act
    val fragment:Fragment = frg

    override fun onPreExecute() {
        super.onPreExecute()
        progressCircle.show(dialogContext)
    }

    override fun doInBackground(vararg params: String?): ArrayList<Information> {

        try{
            val doc = Jsoup.connect(params[0]).get()
            val data = doc.select("#main_table_countries_today > tbody > tr")

            val dayInfo = doc.select("div.content-inner")

            var cnt = 0
            for (item in dayInfo.select("div")){
                if (cnt == 2){
                    item.text().let { info ->
                        val infoSplit = info.split(" ")

                        val year = infoSplit[4].substring(0, infoSplit[4].length - 1) // 년도

                        val month = infoSplit[2].let {mon->
                            when{
                                mon == "January" -> "01"
                                mon == "February" -> "02"
                                mon == "March" -> "03"
                                mon == "April" -> "04"
                                mon == "May" -> "05"
                                mon == "June" -> "06"
                                mon == "July" -> "07"
                                mon == "August" -> "08"
                                mon == "September" -> "09"
                                mon == "October" -> "10"
                                mon == "November" -> "11"
                                mon == "December" -> "12"
                                else -> mon
                            }
                        }

                        val day = infoSplit[3].substring(0, infoSplit[3].length - 1) // 날짜
                        val time = infoSplit[5].substring(0, infoSplit[2].length) // 시간
                        val worldTime = infoSplit[6] //세계표준시간

                        Singleton.worldDayInfo = "   ( " + year + ". " + month + ". " + day + "  " + time + " " + "세계표준시간" + " )"
                    }
                    break
                }
                cnt++
            }

            var country :String
            var totalCases : String
            var newCases : String
            var totalDeaths :String
            var newDeaths : String
            var totalRecovered : String

            // country cnt
            var countCnt :Int = 1

            for (datum in data){

                country = datum.select("td")[0].text().trim()
                totalCases = datum.select("td")[1].text().trim()
                newCases = datum.select("td")[2].text().trim()
                totalDeaths = datum.select("td")[3].text().trim()
                newDeaths = datum.select("td")[4].text().trim()
                totalRecovered = datum.select("td")[5].text().trim()

                //World 제거
                if (country == "World" || country == "Total:"){
                    continue
                }

                // 영어 -> 한글
                country = CountryTrans(country)

                if(totalCases.length == 0){
                     totalCases += '0'
                }
                if (totalDeaths.length == 0){
                    totalDeaths += '0'
                }
                if (totalRecovered.length == 0){
                    totalRecovered += '0'
                }
                if (newCases.length == 0){
                    newCases += "+0"
                }
                if (newDeaths.length == 0){
                    newDeaths += "+0"
                }

                val total = Information(
                    null,
                    country,
                    totalCases + '\n' + newCases,
                    totalDeaths + '\n' + newDeaths,
                    totalRecovered
                )

                //대륙별 추가
                if (country == "Europe" || country == "North America" || country == "Asia" || country == "South America" ||
                    country == "Africa" || country == "Oceania"){

                    Singleton.continent?.add(total)
                    continue
                }

                infoList.add(total)

                countCnt++
            }


            val splitData  = { c: String->
                val case = c.split('\n')
                case[0].replace(",","")
            }

            // totalCase reverse sort
            infoList.sortByDescending { splitData(it.totalCases).toInt() }

            // numberling
           for (i in 0 until infoList.size){
                infoList[i].num = i+1
            }

            Singleton.continent?.let { cont->
                for (i in 0 until cont.size){
                    cont[i].num = i+1
                }
            }

            // total data addtotalCases
            data[data.size-1].let {total->
                val totC = total.select("td")[1].text().trim()
                val totD = total.select("td")[3].text().trim()
                val totR = total.select("td")[5].text().trim()
                infoList.add(
                    Information(
                        0,
                        (countCnt - 1).toString(),
                        totC,
                        totD,
                        totR
                    )
                )
            }

        }catch (e : IOException) {
            e.printStackTrace()
        }

        return infoList
    }

    override fun onProgressUpdate(vararg values: String?) {
        super.onProgressUpdate(*values)
    }

    override fun onPostExecute(result: ArrayList<Information>) {
        super.onPostExecute(result)
        Singleton.coronaList = result

        progressCircle.dialog.dismiss()

        currentActivity.supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment, fragment.javaClass.simpleName)
            .commitAllowingStateLoss()
    }

}

