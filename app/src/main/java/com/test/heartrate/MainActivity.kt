package com.test.heartrate

import android.animation.AnimatorSet
import android.media.Image
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.activity.ComponentActivity
import android.animation.ObjectAnimator
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import kotlin.math.roundToLong

class MainActivity : ComponentActivity() {
    private lateinit var  heartImage: ImageView
    private lateinit var bpmSeekBar: SeekBar
    private lateinit var bpmText: TextView
    private var bpm = 80
    private val handle = Handler(Looper.getMainLooper())
    private var isAnimating = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        heartImage = findViewById(R.id.heartImage)
        bpmSeekBar = findViewById(R.id.bpmSeekBar)
        bpmText = findViewById(R.id.bpmText)

        bpmText.text = "BPM: $bpm"

        startHeartAnimation(bpm)

        bpmSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (progress > 40) {
                    bpm = progress
                    bpmText.text = "BPM: $bpm"
                    handle.removeCallbacksAndMessages(null)
                    isAnimating = false
                    startHeartAnimation(bpm)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun startHeartAnimation(bpm: Int) {
        if (isAnimating) return
        isAnimating = true

        val beatInterval = (60000.0 / bpm).roundToLong()

        val beatRunnable = object : Runnable {
            override fun run() {
               playHeartBeatOnce()
                handle.postDelayed(this, beatInterval)

            }

        }
        handle.post(beatRunnable)

    }

    private fun playHeartBeatOnce(){
        val scaleUpX = ObjectAnimator.ofFloat(heartImage, "scaleX", 1f, 1.3f)
        val scaleUpY = ObjectAnimator.ofFloat(heartImage, "scaleY", 1f, 1.3f)
        val scaleDownX = ObjectAnimator.ofFloat(heartImage, "scaleX", 1.3f, 1f)
        val scaleDownY = ObjectAnimator.ofFloat(heartImage, "scaleY", 1.3f, 1f)

        scaleUpX.duration = 150
        scaleUpY.duration = 150
        scaleDownX.duration = 150
        scaleDownY.duration = 150

        val animatorSet = AnimatorSet()
        animatorSet.play(scaleUpX).with(scaleUpY)
        animatorSet.play(scaleDownX).with(scaleDownY).after(scaleUpX)
        animatorSet.start()

    }
}