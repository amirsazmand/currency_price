plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")

}

android {
    buildFeatures {
        viewBinding = true

    }

    namespace = "com.example.dunipool"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.dunipool"
        minSdk = 24
        targetSdk = 33
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    // coroutine
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")


    //lifecycle
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")



    // glide
    implementation ("jp.wasabeef:glide-transformations:4.3.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:adapter-rxjava3:2.9.0")


    // chart
    implementation("com.robinhood.spark:spark:1.2.0")

    // swipe refresh
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // sweet alert dialog
    implementation ("com.github.f0ris.sweetalert:library:1.5.1")


    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}