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
    private var handshakeInProgress = false
    private var personDetected = false
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
        setupTouchSensors(qiContext)
        setupPersonDetection(qiContext)
        sayText("I am ready!")
    }

    private fun setupPersonDetection(qiContext: QiContext) {
        try {
            val humanAwareness = qiContext.humanAwareness
            humanAwareness.addOnHumansAroundChangedListener { humans ->
                if (humans.isNotEmpty() && !personDetected && !handshakeInProgress && !isDancing) {
                    personDetected = true
                    handler.post {
                        findViewById<TextView>(R.id.statusText).text = "👀 Person detected!"
                    }
                    Thread.sleep(2000)
                    Thread { initiateHandshake(qiContext) }.start()
                } else if (humans.isEmpty()) {
                    personDetected = false
                    handler.post {
                        if (!isDancing) findViewById<TextView>(R.id.statusText).text = "Choose a dance!"
                    }
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("Pepper", "Person detection failed: " + e.message)
        }
    }

    private fun initiateHandshake(qiContext: QiContext) {
        if (handshakeInProgress) return
        handshakeInProgress = true

        handler.post {
            findViewById<TextView>(R.id.statusText).text = "👋 Handshake initiated!"
        }

        try {
            sayText("Nice to meet you! Let's shake hands!")
            Thread.sleep(2500)
            val animation = AnimationBuilder.with(qiContext)
                .withResources(R.raw.handshake_b001)
                .build()
            val animate = AnimateBuilder.with(qiContext)
                .withAnimation(animation)
                .build()
            animate.run()
        } catch (e: Exception) {
            android.util.Log.e("Pepper", "Handshake failed: " + e.message)
        } finally {
            handshakeInProgress = false
            handler.post {
                if (!isDancing) findViewById<TextView>(R.id.statusText).text = "Choose a dance!"
            }
        }
    }

    private fun setupTouchSensors(qiContext: QiContext) {
        val headSensor: TouchSensor = qiContext.touch.getSensor("Head/Touch")
        headSensor.addOnStateChangedListener { touchState ->
            if (touchState.touched && !isDancing) {
                Thread { runAnimation(qiContext, R.raw.wave_right_b001) }.start()
            }
        }

        val leftHandSensor: TouchSensor = qiContext.touch.getSensor("LHand/Touch")
        leftHandSensor.addOnStateChangedListener { touchState ->
            if (touchState.touched && !isDancing) {
                Thread { runAnimation(qiContext, R.raw.no_b001) }.start()
            }
        }

        val rightHandSensor: TouchSensor = qiContext.touch.getSensor("RHand/Touch")
        rightHandSensor.addOnStateChangedListener { touchState ->
            if (touchState.touched && !isDancing) {
                Thread { runAnimation(qiContext, R.raw.point_b001) }.start()
            }
        }

        val bumperSensor: TouchSensor = qiContext.touch.getSensor("Bumper/FrontLeft")
        bumperSensor.addOnStateChangedListener { touchState ->
            if (touchState.touched) {
                if (isDancing) stopDancing()
                else qiContext.let { startDancing(it, "Dance 1") }
            }
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

    private fun runAnimation(qiContext: QiContext, resourceId: Int) {
        try {
            val animation = AnimationBuilder.with(qiContext)
                .withResources(resourceId)
                .build()
            val animate = AnimateBuilder.with(qiContext)
                .withAnimation(animation)
                .build()
            animate.run()
        } catch (e: Exception) {
            android.util.Log.e("Pepper", "Animation failed: " + e.message)
        }
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
