# Pepper-Week15-Motions

## What we did this week
This week we built an Android app for the SoftBank Pepper robot. The goal was to implement basic robot motions triggered by touch sensors, and add obstacle detection so the robot reacts when something is detected.

## Features

### Touch Sensor Animations
Pepper has touch sensors on its body. When touched, different animations play:
- **Head** → Hello animation (Pepper waves hello)
- **Left hand** → Nice reaction animation (positive response)
- **Right hand** → Sad reaction animation (sad response)
- **Front bumper** → Funny dance animation

### Obstacle Detection
- When Pepper's front bumper detects a collision, Pepper says **"Watch out! Obstacle detected!"**
- All touch animations are blocked while obstacle is active
- After 3 seconds, Pepper says **"All clear!"** and resumes normal behavior
- Note: We used bumper sensors instead of camera/laser detection because our Pepper runs NAOqi 2.3.2 which does not support humanAwareness API

### Greeting
- When the app starts, Pepper says **"I am ready! Touch me!"**

## How it works (code explanation)

The app uses QiSDK to communicate with Pepper. The main file is `MainActivity.kt`.

1. `onRobotFocusGained` - called automatically when Pepper is ready. Sets up sensors and says greeting.
2. `setupTouchSensors` - listens to Head, Left hand, Right hand sensors. Each triggers a different animation.
3. `setupBumperObstacle` - listens to front bumper. When triggered, blocks animations and announces obstacle.
4. `runAnimation` - loads a .qianim file from the raw folder and runs it on Pepper using AnimateBuilder.
5. `sayText` - makes Pepper speak using SayBuilder.

## Animation files
Animations are stored as `.qianim` files in `app/src/main/res/raw/`:

| File | Animation | Trigger |
|------|-----------|---------|
| wave_right_b001.qianim | Hello wave | Head touch |
| no_b001.qianim | Nice reaction | Left hand touch |
| point_b001.qianim | Sad reaction | Right hand touch |
| dance_b001.qianim | Funny dance | Bumper touch |

Animations were downloaded from:
- https://github.com/aldebaran/qisdk-tutorials
- https://github.com/softbankrobotics-labs/pepper-core-anims

## Tech stack

| Tool | Version |
|------|---------|
| Android Studio | Panda 2 – 2025.3.2 |
| QiSDK | 1.7.5 |
| Gradle | 8.9 |
| Kotlin | 1.9.20 |
| Min SDK | API 23 (Android 6.0) |
| Robot | SoftBank Pepper (NAOqi 2.3.2) |

## Connect and run

```bash
# Connect to Pepper tablet
adb connect 192.168.0.183:5555

# Build and install
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew assembleDebug
adb -s 192.168.0.183:5555 install -r app/build/outputs/apk/debug/app-debug.apk
adb -s 192.168.0.183:5555 shell am start -n com.example.pepperhelloworld/.MainActivity

# Stop autonomous life (required for motions)
adb -s 192.168.0.183:5555 shell am broadcast -a com.softbankrobotics.qisdk.action.STOP_AUTONOMOUS_LIFE
```

## IP addresses
| Device | IP |
|--------|-----|
| Pepper robot | 192.168.0.178 |
| Pepper tablet | 192.168.0.183 |
