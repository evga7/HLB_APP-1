package com.HLB.comagnifier.singleton

import android.content.Context
import android.location.LocationManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity

import com.HLB.comagnifier.korea.FragmentKorea
import com.HLB.comagnifier.progresscircle.CustomProgressCircle
import com.HLB.comagnifier.world.worldData.Information
import com.HLB.comagnifier.R
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL


class Singleton {

    // 동반자 객체, 코틀린에는 static 이 없음 대신 그 대안으로 object 를 제공함.
    companion object {
        // 네트워크 상태
        var isNetworkConnected: Boolean? = null

        //world
        var coronaList: ArrayList<Information>? = null
        var continent: ArrayList<Information>? = null
        var coronaFlag:Boolean = false
        var countrySum:String? = null
        var totalCasesSum:String? = null
        var totalDeathsSum:String? = null
        var totalRecoveredSum:String? = null
        var worldDayInfo:String? = null
        var coList: ArrayList<FragmentKorea.Item>?=null
        var coList2: ArrayList<FragmentKorea.Item>?=null
        var coList3: ArrayList<FragmentKorea.CityItem> ?=null
        var backframent = 0

        lateinit var locationManager: LocationManager
        lateinit var Activity: AppCompatActivity
        var search: Boolean = true

        val progressCircle =
            CustomProgressCircle()
        // 사용자 기기의 위치 정보를 받아올 객체 인스턴스


        var nDialog: Boolean = true

        var permissionAgreement: Boolean = false

        //공공데이터 정보를 얻어옴.
       
    }
}