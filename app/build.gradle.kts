plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.pepperhelloworld"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.yazan.pepperhelloworld"
        minSdk = 23
        targetSdk = 35
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

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/androidx.versionedparcelable_versionedparcelable.version"
            excludes += "META-INF/androidx.localbroadcastmanager_localbroadcastmanager.version"
            pickFirsts += "**/*.so"
        }
    }
}

configurations.all {
    resolutionStrategy {
        force("androidx.versionedparcelable:versionedparcelable:1.1.1")
        force("androidx.localbroadcastmanager:localbroadcastmanager:1.1.0")
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("androidx.versionedparcelable:versionedparcelable:1.1.1")
    implementation("com.aldebaran:qisdk:1.7.5") {
        exclude(group = "androidx.versionedparcelable")
        exclude(group = "androidx.localbroadcastmanager")
    }
    implementation("com.aldebaran:qisdk-design:1.7.5") {
        exclude(group = "androidx.versionedparcelable")
        exclude(group = "androidx.localbroadcastmanager")
    }
}