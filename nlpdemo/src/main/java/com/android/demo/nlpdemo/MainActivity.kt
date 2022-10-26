package com.android.demo.nlpdemo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun action_preview(view: View) {
        startActivity(Intent(this, DataBaseActivity::class.java))
    }

    fun action_custom(view: View) {

    }

}