package com.example.pepperweek14;

import android.os.Bundle;
import android.widget.ImageView;
import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.AnimateBuilder;
import com.aldebaran.qi.sdk.builder.AnimationBuilder;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.object.actuation.Animate;
import com.aldebaran.qi.sdk.object.actuation.Animation;
import com.aldebaran.qi.sdk.object.conversation.Say;

public class MainActivity extends RobotActivity implements RobotLifecycleCallbacks {

    private QiContext qiContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QiSDK.register(this, this);
        setContentView(R.layout.activity_main);

        ImageView waveImage = findViewById(R.id.waveImage);
        waveImage.setOnClickListener(v -> {
            if (qiContext != null) {
                new Thread(() -> {
                    try {
                        Say say = SayBuilder.with(qiContext)
                                .withText("Hello! Nice to meet you!")
                                .build();
                        say.run();

                        Animation animation = AnimationBuilder.with(qiContext)
                                .withResources(R.raw.animation)
                                .build();
                        Animate animate = AnimateBuilder.with(qiContext)
                                .withAnimation(animation)
                                .build();
                        animate.run();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        this.qiContext = qiContext;
    }

    @Override
    public void onRobotFocusLost() {
        this.qiContext = null;
    }

    @Override
    public void onRobotFocusRefused(String reason) {}

    @Override
    protected void onDestroy() {
        QiSDK.unregister(this, this);
        super.onDestroy();
    }
}