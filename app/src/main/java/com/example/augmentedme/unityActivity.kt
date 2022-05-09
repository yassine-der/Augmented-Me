package com.example.augmentedme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.unity3d.player.UnityPlayerActivity
class unityActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unity)
        val intent = Intent(this, UnityPlayerActivity::class.java)
        startActivity(intent)

    }
}