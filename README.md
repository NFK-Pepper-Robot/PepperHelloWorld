# Pepper-Week15-Dance

## Week 15 Task
Implement a dance behavior. The robot starts dancing when triggered via the tablet.

## Collaboration
- **Zkaria** — Implemented the dance behavior on the robot (animations, QiSDK logic, bumper trigger)
- **Yazan** — Implemented the tablet UI (buttons and visual display)

## What the app does
- Tablet shows two dance buttons: **Dance 1** and **Dance 2**
- Press a button on the tablet → Pepper starts dancing
- Press the front bumper → Pepper starts/stops dancing
- Music visualizer animates on the tablet while Pepper dances
- Press **STOP** to stop the dance
- Pepper says "Let's dance!" before starting and "Dance finished!" when stopped

## Dance animations
| Button | Animation file | Description |
|--------|---------------|-------------|
| Dance 1 | dance_b001.qianim | Aldebaran standard dance |
| Dance 2 | dance_b002.qianim | Funny dance animation |

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
