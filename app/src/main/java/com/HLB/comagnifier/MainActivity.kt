package com.HLB.comagnifier

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import com.HLB.comagnifier.help.FragmentHelp
import com.HLB.comagnifier.korea.FragmentKorea
import com.HLB.comagnifier.korea.koreaAsync.koreaAsyncMainData
import com.HLB.comagnifier.singleton.Singleton
import com.HLB.comagnifier.world.FragmentWorld
import com.HLB.comagnifier.world.worldAsync.WorldCrawling
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.update_dialog.view.*
import nl.joery.animatedbottombar.AnimatedBottomBar
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private var content: FrameLayout? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    val connectState = NetworkConnectionState(this@MainActivity)

    var currentId = 0
    var currentfragment=Fragment()
    var mBackWait:Long = 0
    private var permission_list = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    override fun onBackPressed() {
        if (Singleton.backframent ==0) {
            if (System.currentTimeMillis() - mBackWait >= 2000) {
                mBackWait = System.currentTimeMillis()
                Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            } else {
                System.exit(0)
            }
        }
        else
        {
            super.onBackPressed()
        }
    }




    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val cm: ConnectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = cm.activeNetworkInfo ?: null
        if (networkInfo==null)
        {
            val dialogView =this.layoutInflater.inflate(R.layout.update_dialog, null)
            val builder = android.app.AlertDialog.Builder(this).setView(dialogView)
            builder.show()
            dialogView.infoDialogText.setText("인터넷이 접속되어있어야 가능합니다\n인터넷에 접속해주세요.")
            dialogView.updateOkButton.setOnClickListener {
                System.exit(0)
            }
        }
        else {
            if (Singleton.coList == null) {
                val fragment = FragmentKorea.Companion.newInstance()
                currentfragment = FragmentKorea()
                koreaAsyncMainData(
                    this,
                    this,
                    fragment
                ).execute("http://ncov.mohw.go.kr")
            }
            Log.d("onSaveInstanceState", "${savedInstanceState} !!!!!!!!!!!!!!!!!!!!!!!!!!!")

            // locationManager 를 이용하려면 메인액티비티에서 getSystemService 를 받아와야 함.
            Singleton.locationManager =
                getSystemService(Context.LOCATION_SERVICE) as LocationManager
            Singleton.Activity = this


            content = findViewById(R.id.frameLayout)

            navigationView.setOnTabInterceptListener(object :
                AnimatedBottomBar.OnTabInterceptListener {
                override fun onTabIntercepted(
                    lastIndex: Int,
                    lastTab: AnimatedBottomBar.Tab?,
                    newIndex: Int,
                    newTab: AnimatedBottomBar.Tab
                ): Boolean {
                    if (newTab.id == R.id.korea) {
                        currentfragment = FragmentKorea.Companion.newInstance()
                        Singleton.backframent = 0
                        addFragment(currentfragment)
                        currentId=0
                    }
                    if (newTab.id == R.id.world) {
                        currentId=1
                        currentfragment = FragmentWorld()
                        Singleton.backframent = 0
                        if (Singleton.coronaList == null) {
                            try {
                                WorldCrawling(
                                    this@MainActivity,
                                    this@MainActivity,
                                    currentfragment
                                ).execute("https://www.worldometers.info/coronavirus/")
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        } else {
                            addFragment(currentfragment)
                        }

                    }
                    if (newTab.id == R.id.help) {

                        Singleton.backframent = 0
                        currentfragment = FragmentHelp()
                        addFragment(currentfragment)
                        currentId=3
                    }
                    return true
                }

            })


            //addFragment(fragment)
            //사용자에게 위치 권한 설정을 물어봄.
        }

    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            //.setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
            .replace(R.id.frameLayout, fragment, fragment.javaClass.simpleName).commit()
    }



    // 사용자에게 권한을 확인할 함수. onCreate 에서 호출, 마시멜로우 이상부터 확인해야함.




    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("onSaveInstanceState","${outState} !!!!!!!!!!!!!!!!!!!!!!!!!!!")
    }



}

