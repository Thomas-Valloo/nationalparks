import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
}

val keysPropertiesFile = rootProject.file("keys.properties")
val keysProperties = Properties()
keysProperties.load(keysPropertiesFile.inputStream())

android {
    namespace = "com.valloo.demo.nationalparks"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.valloo.demo.nationalparks"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "NPS_API_KEY", keysProperties["NPS_API_KEY"] as String)

        testInstrumentationRunner = "com.valloo.demo.nationalparks.InstrumentedTestRunner"

        vectorDrawables.useSupportLibrary = true
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
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
        dataBinding = true
    }
    packaging {
        resources {
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/LICENSE-notice.md"
        }
    }
    testOptions {
        packaging {
            jniLibs {
                useLegacyPackaging = true
            }
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")

    implementation("androidx.constraintlayout:constraintlayout:2.1.4")


    // Lifecycle
    val lifecycleVersion = "2.6.2"
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")

    // Navigation
    val navigationVersion = "2.7.5"
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")
    androidTestImplementation("androidx.navigation:navigation-testing:$navigationVersion")

    // Retrofit
    val retrofitVersion = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.retrofit2:adapter-rxjava3:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofitVersion")
    val okHttpVersion = "4.11.0"
    implementation("com.squareup.okhttp3:logging-interceptor:$okHttpVersion")
    implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")

    //Moshi Library
    implementation("com.squareup.moshi:moshi-kotlin:1.9.3")

    // Hilt : dependency injection
    val hiltDaggerVersion = "2.49"
    implementation ("com.google.dagger:hilt-android:$hiltDaggerVersion")
    kapt ("com.google.dagger:hilt-compiler:$hiltDaggerVersion")
    // For Robolectric tests.
//    testImplementation("com.google.dagger:hilt-android-testing:$hiltDaggerVersion")
    // ...with Kotlin.
    kaptTest("com.google.dagger:hilt-android-compiler:$hiltDaggerVersion")
    // For instrumented tests.
    androidTestImplementation("com.google.dagger:hilt-android-testing:$hiltDaggerVersion")
    // ...with Kotlin.
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:$hiltDaggerVersion")
    val hiltAndroidxVersion = "1.1.0"
    implementation("androidx.hilt:hilt-common:$hiltAndroidxVersion")
    implementation("androidx.hilt:hilt-work:$hiltAndroidxVersion")
    kapt("androidx.hilt:hilt-compiler:$hiltAndroidxVersion")

    // RxJava
    implementation("io.reactivex.rxjava3:rxjava:3.1.8")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")

    // Paging
    val pagingVersion = "3.2.1"
    implementation("androidx.paging:paging-runtime-ktx:$pagingVersion")
    implementation("androidx.paging:paging-rxjava3:$pagingVersion")
    androidTestImplementation("androidx.paging:paging-testing:$pagingVersion")


    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Swiperefreshlayout
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$roomVersion")
    // To use Kotlin Symbol Processing (KSP)
//    ksp("androidx.room:room-compiler:$room_version")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$roomVersion")
    // optional - RxJava3 support for Room
    implementation("androidx.room:room-rxjava3:$roomVersion")
    // optional - Guava support for Room, including Optional and ListenableFuture
    implementation("androidx.room:room-guava:$roomVersion")
    // optional - Test helpers
    testImplementation("androidx.room:room-testing:$roomVersion")
    // optional - Paging 3 Integration
    implementation("androidx.room:room-paging:$roomVersion")

    val workVersion = "2.9.0"
    // Kotlin + coroutines
    implementation ("androidx.work:work-runtime-ktx:$workVersion")
    implementation ("androidx.work:work-rxjava3:$workVersion")
    // optional - Test helpers
    androidTestImplementation("androidx.work:work-testing:$workVersion")
    // optional - Multiprocess support
    implementation ("androidx.work:work-multiprocess:$workVersion")

    // Preference
    implementation ("androidx.preference:preference-ktx:1.2.1")

    // Junit
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Mockk
    // Not using version 1.13.8 because of this :
    //    https://github.com/mockk/mockk/issues/1168
    val mockkVersion = "1.13.7"
    testImplementation("io.mockk:mockk:$mockkVersion")
    androidTestImplementation("io.mockk:mockk-android:$mockkVersion")

    // Kotlin
    val fragmentVersion = "1.6.2"
    implementation("androidx.fragment:fragment-ktx:$fragmentVersion")
    // Testing Fragments in Isolation
    debugImplementation("androidx.fragment:fragment-testing:$fragmentVersion")

    testImplementation("org.instancio:instancio-junit:3.7.1")
    androidTestImplementation("org.instancio:instancio-junit:3.7.1")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}

