# 🤖 Pepper Hello World

Our first Android app for the SoftBank Pepper robot. We got Pepper to say "Hello World" out loud using Java and the QiSDK library.




## 🛠️ Tools & Versions
- **Android Studio:** Panda 2 (2025.3.2)
- **Language:** Java
- **Minimum SDK:** API 23 (Android 6.0)
- **QiSDK:** 1.7.5
- **NAOqi version:** 2.9.5.172

## 📚 Libraries used
```gradle
implementation("com.aldebaran:qisdk:1.7.5")
implementation("com.aldebaran:qisdk-design:1.7.5")
```
Added via this Maven repository:
```
http://android.aldebaran.com/sdk/maven
```

## 📱 How to activate Developer Mode on Pepper's tablet
1. Go to **Settings** on the tablet
2. Scroll down to **About device**
3. Tap **Build number** 7 times
4. Developer options is now unlocked
5. Go to **Developer options**
6. Enable **USB debugging**
7. Enable **ADB over network**



The Peper SDK
```
maven {
    url = uri("http://android.aldebaran.com/sdk/maven")
    isAllowInsecureProtocol = true
}
``` 

## 🔌 How to connect your computer to Pepper
1. Make sure your computer and Pepper are on the **same Wi-Fi network**
2. Open terminal and navigate to ADB:
```
cd C:\Users\...\AppData\Local\Android\Sdk\platform-tools
```
3. Connect via ADB:
```
.\adb connect 192.168.0.183
```
4. Approve the connection popup on Pepper's screen
5. In Android Studio, select **192.168.0.183** as the target device

## ⚙️ How it works

When the app starts, it connects to Pepper's brain (NAOqi) using QiSDK. The key code in `MainActivity.java`:
```java
public void onRobotFocusGained(QiContext qiContext) {
    Say say = SayBuilder.with(qiContext)
            .withText("Hello World!")
            .build();
    say.run();
}
```

`onRobotFocusGained` triggers automatically when the app connects to Pepper. `SayBuilder` creates a speech action and `.run()` makes Pepper speak.
