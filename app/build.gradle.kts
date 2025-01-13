plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.mealplanner"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mealplanner"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation("com.squareup.picasso:picasso:2.71828")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.0")
    androidTestImplementation(libs.espresso.core)
}