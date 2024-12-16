plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("kapt")
}

android {
    namespace = "com.app.vakna"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.app.vakna"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true
        dataBinding = true
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md"
            excludes += "META-INF/LICENSE"
            excludes += "META-INF/NOTICE.md"
            excludes += "META-INF/NOTICE"
        }
    }

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            enableUnitTestCoverage = true
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.databinding.compiler)
    implementation(libs.androidx.preference.ktx)
    testImplementation(libs.junit)
    testImplementation("org.mockito:mockito-core:4.8.0")
    implementation("androidx.databinding:databinding-runtime:7.0.0")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    androidTestImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    kapt("com.github.bumptech.glide:compiler:4.12.0")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("org.robolectric:robolectric:4.7.3")
    testImplementation("androidx.test:core:1.4.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
    androidTestImplementation("org.mockito:mockito-android:4.8.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.test:core:1.4.0")
    implementation(kotlin("test"))
    implementation("androidx.fragment:fragment-ktx:1.6.1")
    androidTestImplementation("org.jacoco:org.jacoco.core:0.8.1")
}
