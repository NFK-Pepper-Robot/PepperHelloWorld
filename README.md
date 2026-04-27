# Pepper-Week16-Interaction

## Week 16 Task
Implement touch interaction: when the robot is touched, it performs a simple gesture. Add person detection and gaze detection; if the person is looking at the robot, the robot initiates a handshake.

## What the app does

### Touch Interaction
- **Head** → Hello animation
- **Left hand** → Nice reaction animation  
- **Right hand** → Sad reaction animation
- **Front bumper** → Start/stop dance

### Person Detection & Handshake
- Pepper detects when a person is standing in front of it
- Tablet shows "👀 Person detected!"
- After 2 seconds → Pepper says "Nice to meet you! Let's shake hands!"
- Pepper performs handshake animation (extends right hand)
- Tablet shows "👋 Handshake initiated!"

### Dance
- Two dance buttons on tablet: Dance 1 and Dance 2
- Music visualizer animates while Pepper dances
- Stop button to end dance

## Tech stack
| Tool | Version |
|------|---------|
| Android Studio | Panda 2 – 2025.3.2 |
| QiSDK | 1.7.5 |
| Gradle | 8.9 |
| Kotlin | 1.9.20 |
| Robot | SoftBank Pepper (NAOqi 2.3.2) |

## How to run
```bash
adb connect 192.168.0.183:5555
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew assembleDebug
adb -s 192.168.0.183:5555 install -r app/build/outputs/apk/debug/app-debug.apk
adb -s 192.168.0.183:5555 shell am start -n com.example.pepperhelloworld/.MainActivity
```
