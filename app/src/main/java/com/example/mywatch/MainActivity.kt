package com.example.mywatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.watch.view.WatchFragment

class MainActivity : AppCompatActivity() {

    private val watchFragment = WatchFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.app_container, watchFragment)
            .commit()
    }
}