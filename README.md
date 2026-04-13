# Pepper-Week15-Motions

Android app for SoftBank Pepper robot using QiSDK.

## Features
- Touch sensors trigger animations (Head, Hands, Bumper)
- Obstacle detection via bumper sensors
- Animations: Hello, Nice reaction, Sad reaction, Funny dance

## Tech
- Android Studio Panda 2
- QiSDK 1.7.5
- Gradle 8.9
- Pepper NAOqi 2.3.2

## Connect to Pepper
```bash
adb connect 192.168.0.183:5555
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew assembleDebug
adb -s 192.168.0.183:5555 install -r app/build/outputs/apk/debug/app-debug.apk
adb -s 192.168.0.183:5555 shell am start -n com.example.pepperhelloworld/.MainActivity
```
