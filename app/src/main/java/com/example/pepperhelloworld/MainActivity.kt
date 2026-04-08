package com.example.pepperhelloworld

import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.builder.SayBuilder
import com.aldebaran.qi.sdk.builder.AnimateBuilder
import com.aldebaran.qi.sdk.builder.AnimationBuilder
import com.aldebaran.qi.sdk.design.activity.RobotActivity
import com.aldebaran.qi.sdk.`object`.touch.TouchSensor

class MainActivity : RobotActivity(), RobotLifecycleCallbacks {

    private var qiContext: QiContext? = null

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        QiSDK.register(this, this)
        setContentView(R.layout.activity_main)
    }

    override fun onDestroy() {
        QiSDK.unregister(this, this)
        super.onDestroy()
    }

    override fun onRobotFocusGained(qiContext: QiContext) {
        this.qiContext = qiContext
        sayText("I am ready! Touch me!")
        setupTouchSensors(qiContext)
    }

    private fun setupTouchSensors(qiContext: QiContext) {
        val headSensor: TouchSensor = qiContext.touch.getSensor("Head/Touch")
        headSensor.addOnStateChangedListener { touchState ->
            if (touchState.touched) {
                Thread { runAnimation(qiContext, R.raw.wave_right_b001) }.start()
            }
        }

        val leftHandSensor: TouchSensor = qiContext.touch.getSensor("LHand/Touch")
        leftHandSensor.addOnStateChangedListener { touchState ->
            if (touchState.touched) {
                Thread { runAnimation(qiContext, R.raw.no_b001) }.start()
            }
        }

        val rightHandSensor: TouchSensor = qiContext.touch.getSensor("RHand/Touch")
        rightHandSensor.addOnStateChangedListener { touchState ->
            if (touchState.touched) {
                Thread { runAnimation(qiContext, R.raw.point_b001) }.start()
            }
        }

        val bumperSensor: TouchSensor = qiContext.touch.getSensor("Bumper/FrontLeft")
        bumperSensor.addOnStateChangedListener { touchState ->
            if (touchState.touched) {
                Thread { runAnimation(qiContext, R.raw.dance_b001) }.start()
            }
        }
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
            sayText("Sorry, animation failed.")
        }
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
