plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("maven-publish")
}

android {
    namespace = "com.nareshchocha.formz"
    compileSdk = 34

    defaultConfig {
        //applicationId = "com.nareshchocha.formz"
        minSdk = 21
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
}

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "com.github.ChochaNaresh"
            artifactId = "formz"
            version = "0.0.1"

            afterEvaluate {
                from(components["release"])
                /* from {
                     components.release
                 }*/
            }
        }
    }
}