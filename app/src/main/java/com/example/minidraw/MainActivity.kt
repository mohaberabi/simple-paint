package com.example.minidraw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val drawerView = DrawerView(context = this)
        drawerView.systemUiVisibility = SYSTEM_UI_FLAG_FULLSCREEN
        drawerView.contentDescription = getString(R.string.canvasContentDescription)
        setContentView(drawerView)
    }
}