package com.cssq.android_toy_brick

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.tzh.tools.Tools
import com.tzh.tools.activity.*
import com.tzh.tools.fragment.*

import com.tzh.tools.model.WebViewEnum


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        com.tzh.tools.Tools.init(application, "", "1", "001", "7", "100003")
        findViewById<Button>(R.id.button).setOnClickListener {
            //SpeedTestActivity.startActivity(this,null)
            //GregorianToDataActivity.startActivity(this,null,true)
//            JokeActivity.startActivity(this,null,0,true)

            DurationCalculationActivity.startActivity(this,null)
        }
//        supportFragmentManager.beginTransaction().replace(R.id.container, CalendarFragment.newInstance()).commit()

        supportFragmentManager.beginTransaction().replace(R.id.container, BatteryInfoFragment.newInstance()).commit()

    }
}