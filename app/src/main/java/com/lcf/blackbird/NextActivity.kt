package com.lcf.blackbird

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class NextActivity : AppCompatActivity() {

    val mHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        mHandler.postDelayed({
//            print("do something")
////            mHandler.postDelayed({
////                print("do something")
////            }, 2000)
//            finish()
//        }, 2000)

    }


}