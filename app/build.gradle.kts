plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services") version "4.4.1" apply true
}

android {
    namespace = "com.example.pan_pan"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pan_pan"
        minSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.24")
    // import Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    // Firebase Core and Analytics
    implementation("com.google.firebase:firebase-core:21.1.1")
    implementation("com.google.firebase:firebase-analytics")
    // Firebase Authentication and Firestore
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-database:21.0.0")

}