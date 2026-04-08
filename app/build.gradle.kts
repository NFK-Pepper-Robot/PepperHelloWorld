plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.pepperhelloworld"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pepperhelloworld"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation("com.aldebaran:qisdk:1.7.5")
    implementation("com.aldebaran:qisdk-design:1.7.5")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "androidx.core") useVersion("1.6.0")
        if (requested.group == "androidx.appcompat") useVersion("1.3.1")
        if (requested.group == "androidx.fragment") useVersion("1.3.6")
        if (requested.group == "androidx.activity") useVersion("1.2.4")
        if (requested.group == "androidx.lifecycle") useVersion("2.3.1")
    }
}

