# 🤖 Pepper Hello World

Our first Android app for the SoftBank Pepper robot. We got Pepper to say "Hello World" out loud using Java and the QiSDK library.

## How it works

The app connects to Pepper's brain (NAOqi) using the QiSDK. When the app starts, Pepper automatically says "Hello World" through his speakers.

The key part of the code is in `MainActivity.java`:
```java
public void onRobotFocusGained(QiContext qiContext) {
    Say say = SayBuilder.with(qiContext)
            .withText("Hello World!")
            .build();
    say.run();
}
```

`onRobotFocusGained` is triggered automatically when the app connects to Pepper. We then use `SayBuilder` to create a speech action with our text, and `.run()` executes it — making Pepper speak.

## Requirements
- Android Studio
- SoftBank Pepper robot (NAOqi 2.9)
- Both devices on the same Wi-Fi network

## How to run
1. Connect to Pepper via ADB: `.\adb connect 192.168.0.183`
2. Approve the connection on Pepper's screen
3. Press the green play button in Android Studio
4. Select Pepper as the target device
