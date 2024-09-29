plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id ("kotlin-kapt")
}

android {
    namespace = "com.example.weatherproject"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.weatherproject"
        minSdk = 24
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.work.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    //implementation ("com.squareup.retrofit2:adapter-coroutines:2.9.0")
    //GSON
    implementation ("com.google.code.gson:gson:2.10.1")
    //Glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.13.0")
    //room
    implementation ("androidx.room:room-ktx:2.5.0")
    implementation ("androidx.room:room-runtime:2.5.0")
    kapt ("androidx.room:room-compiler:2.5.0")
    //Coroutine
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")
    //ViewModel
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")
    implementation ("com.airbnb.android:lottie:6.0.0")
    //navigation componant
    implementation ("androidx.navigation:navigation-fragment:2.8.1")
    implementation ("androidx.navigation:navigation-ui:2.5.3")

//rounded avatar
    implementation ("de.hdodenhof:circleimageview:3.1.0")

//animation
    implementation ("com.airbnb.android:lottie:6.0.0")

//google play services
    implementation("com.google.android.gms:play-services-location:21.1.0")
    implementation ("com.google.android.libraries.places:places:3.1.0")

    implementation ("org.osmdroid:osmdroid-android:6.1.10")
    //ok http
    implementation ("com.squareup.okhttp3:okhttp:4.10.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")
    //ViewModel
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")

    // InstantTaskExecutorRule
    testImplementation ("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation ("androidx.arch.core:core-testing:2.1.0")

    // AndroidX and Robolectric
    testImplementation( "androidx.test.ext:junit-ktx:1.1.3")
    testImplementation ("androidx.test:core-ktx:1.4.0")
    testImplementation ("org.robolectric:robolectric:4.8")

    // AndroidX Test - JVM testing
    testImplementation ("androidx.test:core-ktx:1.4.0")
    //testImplementation "androidx.test.ext:junit:$androidXTestExtKotlinRunnerVersion"

    // AndroidX Test - Instrumented testing
    androidTestImplementation ("androidx.test.:1.1.3")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.4.0")

    //Timber
    implementation ("com.jakewharton.timber:timber:5.0.1")

    // hamcrest
    testImplementation ("org.hamcrest:hamcrest:2.2")
    testImplementation ("org.hamcrest:hamcrest-library:2.2")
    androidTestImplementation ("org.hamcrest:hamcrest:1.3")
    androidTestImplementation ("org.hamcrest:hamcrest-library:1.3")

    // Dependencies for local unit tests
    testImplementation ("junit:junit:4.13.2")
    testImplementation ("org.hamcrest:hamcrest-all:1.3")
    testImplementation ("androidx.arch.core:core-testing:2.1.0")
    testImplementation ("org.robolectric:robolectric:4.5.1")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")
    androidTestImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")

}