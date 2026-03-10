plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.myapplication100"
    compileSdk = 35 // ปรับให้เข้ากับมาตรฐานปัจจุบัน (ถ้า 36 ยังไม่ปล่อยตัวเต็ม)

    defaultConfig {
        applicationId = "com.example.myapplication100"
        minSdk = 24
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

// ... โค้ดส่วนอื่นๆ คงเดิม ...

    packaging {
        resources {
            // แก้ปัญหา INDEX.LIST และ DEPENDENCIES ที่เจอไปก่อนหน้านี้
            excludes += "/META-INF/INDEX.LIST"
            excludes += "/META-INF/DEPENDENCIES"

            // แก้ปัญหา io.netty.versions.properties (11 ไฟล์) ที่เจอระลอกล่าสุด
            excludes += "/META-INF/io.netty.versions.properties"

            // เพิ่มกลุ่มไฟล์มาตรฐานที่มักจะชนกันบ่อยๆ เพื่อป้องกัน Error ในอนาคต
            excludes += "/META-INF/LICENSE"
            excludes += "/META-INF/LICENSE.txt"
            excludes += "/META-INF/NOTICE"
            excludes += "/META-INF/NOTICE.txt"
            excludes += "/META-INF/ASL2.0"
        }
    }
}

dependencies {
    // Core
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
    implementation("androidx.activity:activity-compose:1.9.0")

    // Compose BOM
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.runtime:runtime")

    // Material 3
    implementation("androidx.compose.material3:material3")

    // Icons
    implementation("androidx.compose.material:material-icons-extended")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Firebase App Distribution
    implementation(libs.firebase.appdistribution.gradle)
    implementation(libs.androidx.runtime)

    implementation("androidx.compose.material:material-icons-extended:1.6.0")
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.compose.remote.creation.core)
    implementation(libs.androidx.compose.ui.text)
    testImplementation(kotlin("test"))
}