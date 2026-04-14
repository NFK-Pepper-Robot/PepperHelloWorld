package com.example.pepperhelloworld

import android.animation.ValueAnimator
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.TextView
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.builder.AnimateBuilder
import com.aldebaran.qi.sdk.builder.AnimationBuilder
import com.aldebaran.qi.sdk.builder.SayBuilder
import com.aldebaran.qi.sdk.design.activity.RobotActivity
import com.aldebaran.qi.sdk.`object`.touch.TouchSensor

class MainActivity : RobotActivity(), RobotLifecycleCallbacks {

    private var qiContext: QiContext? = null
    private var isDancing = false
    private var danceThread: Thread? = null
    private var currentDance = R.raw.dance_b001
    private val handler = Handler(Looper.getMainLooper())
    private var visualizerAnimators = mutableListOf<ValueAnimator>()

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        QiSDK.register(this, this)
        setContentView(R.layout.activity_main)
        setupButtons()
    }

    override fun onDestroy() {
        QiSDK.unregister(this, this)
        stopVisualizer()
        super.onDestroy()
    }

    private fun setupButtons() {
        findViewById<Button>(R.id.dance1Button).setOnClickListener {
            currentDance = R.raw.dance_b001
            qiContext?.let { startDancing(it, "Dance 1") }
        }
        findViewById<Button>(R.id.dance2Button).setOnClickListener {
            currentDance = R.raw.dance_b002
            qiContext?.let { startDancing(it, "Dance 2") }
        }
        findViewById<Button>(R.id.stopButton).setOnClickListener {
            stopDancing()
        }
    }

    override fun onRobotFocusGained(qiContext: QiContext) {
        this.qiContext = qiContext
        setupBumperSensor(qiContext)
        sayText("I am ready to dance!")
    }

    private fun setupBumperSensor(qiContext: QiContext) {
        try {
            val bumper: TouchSensor = qiContext.touch.getSensor("Bumper/FrontLeft")
            bumper.addOnStateChangedListener { touchState ->
                if (touchState.touched) {
                    if (isDancing) stopDancing()
                    else qiContext.let { startDancing(it, "Dance 1") }
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("Pepper", "Bumper setup failed: " + e.message)
        }
    }

    private fun startDancing(qiContext: QiContext, danceName: String) {
        if (isDancing) stopDancing()
        isDancing = true

        handler.post {
            findViewById<Button>(R.id.dance1Button).isEnabled = false
            findViewById<Button>(R.id.dance2Button).isEnabled = false
            findViewById<Button>(R.id.stopButton).isEnabled = true
            findViewById<TextView>(R.id.statusText).text = "🎵 $danceName is playing! 🎵"
        }

        startVisualizer()

        danceThread = Thread {
            sayText("Let's dance!")
            Thread.sleep(1000)
            while (isDancing) {
                try {
                    val animation = AnimationBuilder.with(qiContext)
                        .withResources(currentDance)
                        .build()
                    val animate = AnimateBuilder.with(qiContext)
                        .withAnimation(animation)
                        .build()
                    animate.run()
                } catch (e: Exception) {
                    android.util.Log.e("Pepper", "Dance failed: " + e.message)
                    break
                }
            }
        }
        danceThread?.start()
    }

    private fun stopDancing() {
        isDancing = false
        danceThread?.interrupt()
        danceThread = null
        stopVisualizer()

        handler.post {
            findViewById<Button>(R.id.dance1Button).isEnabled = true
            findViewById<Button>(R.id.dance2Button).isEnabled = true
            findViewById<Button>(R.id.stopButton).isEnabled = false
            findViewById<TextView>(R.id.statusText).text = "Choose a dance!"
        }
        sayText("Dance finished!")
    }

    private fun startVisualizer() {
        val barIds = listOf(R.id.bar1, R.id.bar2, R.id.bar3, R.id.bar4, R.id.bar5,
                           R.id.bar6, R.id.bar7, R.id.bar8, R.id.bar9)
        val heights = listOf(20, 60, 100, 60, 20, 60, 100, 60, 20)
        barIds.forEachIndexed { index, id ->
            handler.post {
                val bar = findViewById<View>(id)
                val animator = ValueAnimator.ofInt(heights[index], heights[(index + 4) % 9], heights[index])
                animator.duration = (600 + index * 100).toLong()
                animator.repeatCount = ValueAnimator.INFINITE
                animator.interpolator = AccelerateDecelerateInterpolator()
                animator.addUpdateListener { anim ->
                    val value = anim.animatedValue as Int
                    val params = bar.layoutParams
                    params.height = (value * resources.displayMetrics.density).toInt()
                    bar.layoutParams = params
                }
                animator.start()
                visualizerAnimators.add(animator)
            }
        }
    }

    private fun stopVisualizer() {
        visualizerAnimators.forEach { it.cancel() }
        visualizerAnimators.clear()
    }

    private fun sayText(text: String) {
        qiContext?.let { context ->
            Thread {
                try {
                    SayBuilder.with(context).withText(text).build().run()
                } catch (e: Exception) {
                    android.util.Log.e("Pepper", "Say failed: " + e.message)
                }
            }.start()
        }
    }

    override fun onRobotFocusLost() { qiContext = null }
    override fun onRobotFocusRefused(reason: String) {}
}
